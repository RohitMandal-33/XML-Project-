# Android & XML Concepts in XML Proj

Code-focused walkthrough with small chunks and short explanations. All snippets from this project.

---

## 1. Android Manifest — Launcher & components

The manifest declares every Activity and Service. The launcher activity is the one that opens when the user taps the app icon.

```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.POST_NOTIFICATIONS" />

<activity android:name=".MainActivity" android:exported="true">
    <intent-filter>
        <action android:name="android.intent.action.MAIN" />
        <category android:name="android.intent.category.LAUNCHER" />
    </intent-filter>
</activity>
<activity android:name=".LoginActivity" android:exported="false" />
<service android:name=".MyIntentService" android:exported="false" />
```

- **MAIN + LAUNCHER** = entry point. Only one activity should have this.
- **exported="true"** = other apps can start it (e.g. launcher). **false** = only this app.
- Permissions listed here; dangerous ones (e.g. notifications) are requested at runtime.

---

## 2. Activity — setContentView & lifecycle

An Activity is one screen. You bind an XML layout in `onCreate` and can override lifecycle callbacks to react when the screen is shown, hidden, or destroyed.

```kotlin
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)   // one layout per activity

        // Use savedInstanceState to avoid duplicating work on rotation
        if (savedInstanceState == null) {
            // add initial fragment only once
        }
    }

    override fun onStart() { super.onStart(); Log.d("LIFECYCLE", "onStart") }
    override fun onResume() { super.onResume(); Log.d("LIFECYCLE", "onResume") }
    override fun onPause() { super.onPause(); Log.d("LIFECYCLE", "onPause") }
    override fun onStop() { super.onStop(); Log.d("LIFECYCLE", "onStop") }
    override fun onDestroy() { super.onDestroy(); Log.d("LIFECYCLE", "onDestroy") }
}
```

**Flow:** onCreate (setup) → onStart → onResume (visible) → onPause → onStop → onDestroy. Use `savedInstanceState` to restore state after process death or configuration change.

---

## 3. Explicit Intent — start another screen

Explicit intents target a specific class. Used for navigation inside the app (e.g. Main → Login, Second → ListView).

```kotlin
// MainActivity: open Login or Register
btnLogin.setOnClickListener {
    val intent = Intent(this, LoginActivity::class.java)
    startActivity(intent)
}

// SecondActivity: open ListView, RecyclerView, or Database screen
btnList.setOnClickListener {
    startActivity(Intent(this, ListViewActivity::class.java))
}
btnrecycle.setOnClickListener {
    startActivity(Intent(this, RecyclerViewActivity::class.java))
}
```

**Concept:** `Intent(context, ActivityClass::class.java)` + `startActivity(intent)`.

---

## 4. Implicit Intent — open URL (or other action)

Implicit intents specify an **action** and optional **data**; the system chooses the app (e.g. browser for a URL).

```kotlin
btnOpenWeb.setOnClickListener {
    val intent = Intent(Intent.ACTION_VIEW)
    intent.data = Uri.parse("https://www.google.com")
    startActivity(intent)
}
```

**Concept:** No target class. `ACTION_VIEW` + `data` Uri → system opens browser (or app that handles the URI).

---

## 5. Activity for Result — get data back from a screen

When you start an activity and need a result back (e.g. user picks something and you use it in the caller), use the **Activity Result API**: register a contract and launch with it; the started activity calls `setResult` and `finish()`.

**Caller (MainActivity):**

```kotlin
private val getResult = registerForActivityResult(
    ActivityResultContracts.StartActivityForResult()
) { result ->
    if (result.resultCode == Activity.RESULT_OK) {
        val data = result.data?.getStringExtra("EXTRA_DATA")
        findViewById<TextView>(R.id.textView).text = data
    }
}

btnOpenSecond.setOnClickListener {
    getResult.launch(Intent(this, SecondActivity::class.java))
}
```

**Started activity (SecondActivity):**

```kotlin
btnSend.setOnClickListener {
    val resultIntent = Intent()
    resultIntent.putExtra("EXTRA_DATA", editText.text.toString())
    setResult(Activity.RESULT_OK, resultIntent)
    finish()
}
```

**Concept:** Don’t use deprecated `startActivityForResult`. Use `registerForActivityResult` + contract; pass data with `putExtra` / `getStringExtra` and signal success with `RESULT_OK`.

---

## 6. Fragment — add in Activity

Fragments are reusable UI pieces with their own layout and lifecycle. They live inside a **container** view in the activity. You add or replace them via **FragmentTransaction**.

```kotlin
// MainActivity.onCreate
if (savedInstanceState == null) {
    val fragment = TestFragment.newInstance("Hello from MainActivity")
    supportFragmentManager.beginTransaction()
        .add(R.id.fragment_container, fragment)
        .commit()
}
```

**Layout (activity_main.xml):** the activity must have a container with the same id:

```xml
<FrameLayout
    android:id="@+id/fragment_container"
    android:layout_width="0dp"
    android:layout_height="0dp"
    app:layout_constraintTop_toBottomOf="@id/btnGoToPrefs"
    app:layout_constraintBottom_toBottomOf="parent" />
```

**Concept:** `supportFragmentManager.beginTransaction().add(containerId, fragment).commit()`. Use `supportFragmentManager` when using AndroidX fragments.

---

## 7. Fragment — inflate layout & arguments

The fragment gets its UI from an XML layout via `LayoutInflater`. You pass data into the fragment using a **Bundle** (arguments), usually via a `newInstance` factory.

```kotlin
override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
): View? {
    return inflater.inflate(R.layout.fragment_test, container, false)
}

override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    val message = arguments?.getString(ARG_MESSAGE)
    view.findViewById<TextView>(R.id.txtFragment).text = message
}

companion object {
    private const val ARG_MESSAGE = "arg_message"
    fun newInstance(message: String): TestFragment {
        val fragment = TestFragment()
        fragment.arguments = Bundle().apply { putString(ARG_MESSAGE, message) }
        return fragment
    }
}
```

**Concept:** `inflater.inflate(layoutId, container, false)`; store args in `arguments` in `newInstance` and read them in `onViewCreated` or later.

---

## 8. Fragment → Activity communication (listener)

Fragments should not hold references to the Activity. Use an **interface** that the host Activity implements; the fragment calls the interface method (e.g. when a button is clicked).

**Fragment:** define interface, get listener in `onAttach`, call when needed.

```kotlin
interface OnFragmentInteractionListener {
    fun onMessageFromFragment(message: String)
}
private var listener: OnFragmentInteractionListener? = null

override fun onAttach(context: Context) {
    super.onAttach(context)
    listener = if (context is OnFragmentInteractionListener) context
    else throw RuntimeException("$context must implement OnFragmentInteractionListener")
}
override fun onDetach() { super.onDetach(); listener = null }

// On button click:
listener?.onMessageFromFragment("Hi Activity, from Fragment!")
```

**Activity:** implement the interface.

```kotlin
class MainActivity : AppCompatActivity(), TestFragment.OnFragmentInteractionListener {
    override fun onMessageFromFragment(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
```

**Concept:** Interface in fragment, implemented by activity; fragment uses it in `onAttach`/`onDetach` so it doesn’t leak.

---

## 9. Fragment transaction — replace & back stack

To switch from one fragment to another in the same container, use **replace**. Adding the transaction to the **back stack** lets the user press Back to return to the previous fragment.

```kotlin
view.findViewById<Button>(R.id.btnNext).setOnClickListener {
    parentFragmentManager.beginTransaction()
        .setCustomAnimations(
            android.R.anim.slide_in_left,
            android.R.anim.slide_out_right,
            android.R.anim.slide_in_left,
            android.R.anim.slide_out_right
        )
        .replace(R.id.fragment_container, Test2Fragment())
        .addToBackStack(null)
        .commit()
}
```

**Concept:** `replace()` swaps the fragment; `addToBackStack(null)` records the transaction so Back restores the previous fragment. Optional: `setCustomAnimations()` for enter/exit animations.

---

## 10. DialogFragment

A **DialogFragment** shows a dialog with its own lifecycle. You build the dialog in `onCreateDialog` (e.g. `AlertDialog`) and show it with `show()`.

```kotlin
class MyDialogFragment : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
            .setTitle("Fragment Dialog")
            .setMessage("This is DialogFragment")
            .setPositiveButton("OK", null)
            .create()
    }
}
```

**Showing from another fragment (Test2Fragment):**

```kotlin
btnDialog.setOnClickListener {
    MyDialogFragment().show(parentFragmentManager, "dialog")
}
```

**Concept:** Extend `DialogFragment`, return a `Dialog` from `onCreateDialog`. Use `parentFragmentManager` when showing from a fragment so the dialog is tied to the fragment lifecycle.

---

## 11. XML layout — ConstraintLayout

ConstraintLayout positions views using **constraints** (edges aligned to parent or other views). No nested layout needed for flat hierarchies.

```xml
<androidx.constraintlayout.widget.ConstraintLayout
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:padding="16dp">

    <Button android:id="@+id/btnLogin"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Login"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent" />

    <TextView android:id="@+id/textView"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintTop_toBottomOf="@id/btnLogin" />
</androidx.constraintlayout.widget.ConstraintLayout>
```

**Concept:** Use `app:layout_constraint*_to*Of="parent"` or `"@id/otherView"` to pin edges. Chains (e.g. packed) for groups of views.

---

## 12. ListView + BaseAdapter

**ListView** shows a vertical list. You provide an **Adapter** that returns the number of items and builds each row view. In this project, **StudentAdapter** extends **BaseAdapter** and inflates `item_student.xml`.

**Activity:**

```kotlin
val listView = findViewById<ListView>(R.id.listView)
val students = listOf(
    Student(1, "Alice Johnson", "A", "alice@school.com"),
    Student(2, "Bob Smith", "B+", "bob@school.com")
)
val adapter = StudentAdapter(this, students)
listView.adapter = adapter

listView.setOnItemClickListener { _, _, position, _ ->
    Toast.makeText(this, "Clicked: ${students[position].name}", Toast.LENGTH_SHORT).show()
}
```

**Adapter (concept):** Override `getCount()`, `getItem()`, `getItemId()`, and **getView()**. In `getView()`, reuse `convertView` if non-null and use a ViewHolder to avoid repeated `findViewById`. Inflate `R.layout.item_student` and set name, grade, email from the list.

**Concept:** ListView + BaseAdapter + item layout; reuse convertView and ViewHolder for smooth scrolling.

---

## 13. RecyclerView + Adapter

**RecyclerView** is the modern list: it only creates views for visible items and recycles them. You set a **LayoutManager** (e.g. `LinearLayoutManager`) and an **Adapter** that implements ViewHolder creation and binding.

**Activity:**

```kotlin
val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
adapter = MessageAdapter(messages) { message ->
    Toast.makeText(this, "Opened: ${message.subject}", Toast.LENGTH_SHORT).show()
}
recyclerView.layoutManager = LinearLayoutManager(this)
recyclerView.adapter = adapter
```

**Adapter (concept):** Extend `RecyclerView.Adapter<MessageAdapter.MessageViewHolder>`.

- **onCreateViewHolder:** inflate `R.layout.item_message`, return ViewHolder.
- **onBindViewHolder:** bind data to views and set click listener.
- **getItemCount:** return list size.

ViewHolder holds references to the item’s views so you don’t call `findViewById` in every bind.

**Concept:** RecyclerView + LayoutManager + Adapter with ViewHolder; use `notifyItemChanged` / `notifyDataSetChanged` when data changes.

---

## 14. Service — start and stop

A **Service** runs in the background without a UI. In this project, **MyIntentService** is started from MainActivity; it uses a **HandlerThread** and **Handler** to do work off the main thread and shows Toasts when work starts and finishes.

**Starting and stopping from Activity:**

```kotlin
btnStartService.setOnClickListener {
    startService(Intent(this, MyIntentService::class.java))
}
btnStopService.setOnClickListener {
    stopService(Intent(this, MyIntentService::class.java))
}
```

**Service (concept):** Override `onCreate()` (e.g. create HandlerThread + Handler), `onStartCommand()` (receive work, send Message to Handler, return `START_STICKY` or similar), and `onBind()` (return null if not bound). Do long work on the handler’s thread; call `stopSelf(startId)` when done.

**Concept:** `startService(intent)` / `stopService(intent)`. Service must be declared in the manifest. For heavy work, use a background thread or HandlerThread.

---

## 15. SharedPreferences

**SharedPreferences** is a key–value store for small data (e.g. login state, user name). In this project, **PreferenceManager** wraps it for login state and user info.

**Wrapper class (concept):**

```kotlin
class PreferenceManager(context: Context) {
    private val prefs = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)

    fun saveUserName(name: String) {
        prefs.edit().putString("user_name", name).apply()
    }
    fun getUserName(): String = prefs.getString("user_name", "") ?: ""
    fun setLoggedIn(value: Boolean) { prefs.edit().putBoolean("is_logged_in", value).apply() }
    fun isLoggedIn(): Boolean = prefs.getBoolean("is_logged_in", false)
    fun clearAll() { prefs.edit().clear().apply() }
}
```

**Usage in MainActivity:** If `preferenceManager.isLoggedIn()` then show “Welcome, name” and Logout; else show Login/Register and on login save state and user name.

**Concept:** `getSharedPreferences(name, MODE_PRIVATE)`, then `edit().putString/putBoolean().apply()` and `getString/getBoolean()`. Use for simple, persistent settings.

---

## 16. Room — Entity, DAO, Database

**Room** is a SQLite wrapper: you define **Entity** (table), **DAO** (queries), and **Database** (singleton that provides the DAO). In this project it’s used for user registration and login.

**Entity (table):**

```kotlin
@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val email: String,
    val password: String,
    val name: String,
    val createdAt: Long = System.currentTimeMillis()
)
```

**DAO (queries):**

```kotlin
@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: User): Long
    @Query("SELECT * FROM users WHERE email = :email AND password = :password")
    suspend fun login(email: String, password: String): User?
    @Query("SELECT * FROM users WHERE email = :email")
    suspend fun getUserByEmail(email: String): User?
}
```

**Database (singleton):**

```kotlin
@Database(entities = [User::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    companion object {
        fun getDatabase(context: Context): AppDatabase = /* singleton build */
    }
}
```

**Usage in LoginActivity:** `AppDatabase.getDatabase(this).userDao()` then `lifecycleScope.launch { dao.login(email, password) }`. Use a Repository class to wrap the DAO if you want a single place for data access.

**Concept:** Entity = table, DAO = suspend/Flow functions, Database = Room.databaseBuilder(). DAO calls must run off main thread (coroutines, e.g. `lifecycleScope.launch`).

---

## 17. Resource qualifiers

Different **resource folders** provide different values for the same resource name, depending on configuration (screen size, orientation, night mode, etc.).

**Example in this project:**

- `res/values/dimens.xml` — default: `<dimen name="fab_margin">16dp</dimen>`
- `res/values-w600dp/dimens.xml` — when width ≥ 600dp: `<dimen name="fab_margin">48dp</dimen>`

Other qualifiers: `values-land` (landscape), `values-night` (dark theme), `values-v23` (API 23+). Reference in XML or code as `R.dimen.fab_margin`; the system picks the right file.

**Concept:** Same resource name in different folders; Android selects by device configuration.

---

## 18. Runtime permission

Dangerous permissions (e.g. **POST_NOTIFICATIONS** on Android 13+) must be **requested at runtime**. Use the **Activity Result API** with `RequestPermission()` so the user’s choice is returned in a callback.

```kotlin
private val requestPermissionLauncher = registerForActivityResult(
    ActivityResultContracts.RequestPermission()
) { isGranted: Boolean ->
    if (isGranted) sendNotification()
    else Log.w("NOTIFICATION", "Permission denied")
}

private fun checkPermissionAndShowNotification() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
            == PackageManager.PERMISSION_GRANTED) {
            sendNotification()
        } else {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    } else {
        sendNotification()  // no runtime permission needed on older versions
    }
}
```

**Concept:** Check `checkSelfPermission`; if not granted, call `launcher.launch(PERMISSION)`. Handle the result in the contract callback. For API 33+ notifications, use `POST_NOTIFICATIONS`.

---

*All code and patterns from this project. Each section is one concept with a short explanation and small code chunks.*
