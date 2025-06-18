# E-Commerce-app


This repository contains two Android apps:
- **User App**: For customers to browse and purchase products.
- **Admin App**: For store managers/admins to manage products, orders, and inventory.

---

## 📁 Project Structure

E-Commerce-app/
├── Admin_e_commerce/ → Admin app source code
└── User_e_commerce/ → User app source code


---

## 🔐 Important: AccessToken.kt Not Included

> We have **intentionally excluded** `AccessToken.kt` from version control to protect sensitive Google Cloud credentials.  
> GitHub flagged the original file for containing secrets, so it was removed.

If you're cloning or using this project, you'll need to **create the file yourself** with your own Firebase service account credentials.

---

### 📄 Create `AccessToken.kt` in Admin App

Create the file:

dmin_e_commerce/app/src/main/java/com/example/admin_e_commerce/AccessToken.kt


And paste the following code (replace with your own service account credentials):

```kotlin
import android.util.Log
import com.google.auth.oauth2.GoogleCredentials
import java.io.ByteArrayInputStream
import java.io.IOException
import java.io.InputStream
import java.nio.charset.StandardCharsets

class AccessToken {
    val accessToken: String?
        get() {
            try {
                val jsonString = """{
  "type": "service_account",
  "project_id": "your_project_id",
  "private_key_id": "your_private_key_id",
  "private_key": "-----BEGIN PRIVATE KEY-----\nYOUR_PRIVATE_KEY\n-----END PRIVATE KEY-----\n",
  "client_email": "your_client_email",
  "client_id": "your_client_id",
  "auth_uri": "https://accounts.google.com/o/oauth2/auth",
  "token_uri": "https://oauth2.googleapis.com/token",
  "auth_provider_x509_cert_url": "https://www.googleapis.com/oauth2/v1/certs",
  "client_x509_cert_url": "https://www.googleapis.com/robot/v1/metadata/x509/your_client_email",
  "universe_domain": "googleapis.com"
}"""

                val stream: InputStream = ByteArrayInputStream(
                    jsonString.toByteArray(StandardCharsets.UTF_8)
                )
                val googleCredentials = GoogleCredentials.fromStream(stream)
                    .createScoped(listOf(FIREBASE_MESSAGING_SCOPE))
                googleCredentials.refresh()
                return googleCredentials.accessToken.tokenValue
            } catch (e: IOException) {
                Log.e("error", "Exception: " + e.message)
                return null
            }
        }

    companion object {
        private const val FIREBASE_MESSAGING_SCOPE =
            "https://www.googleapis.com/auth/firebase.messaging"
    }
}
```

🛡️ **IMPORTANT:** Never hardcode real service account credentials in public repositories.  
Instead, use a secure method like reading from `local.properties`, `.env`, or environment variables.


✅ Features
User App:
Browse products by category
Search and filter items
Add to cart and checkout
View order history
Admin App:
Add/update/delete products
Manage inventory & stock
Track customer orders and statuses
🧱 Built With
Kotlin / Java
Firebase (Realtime DB, Auth, Storage)
MVVM Architecture
RecyclerView, Fragments, and Jetpack components
🔧 Setup
Clone the repository:
git clone https://github.com/yourusername/E-Commerce-app.git

Open in Android Studio
Add AccessToken.kt to Admin_e_commerce as described above
Connect to your own Firebase project
🙋‍♂️ Contributing
Pull requests are welcome. For major changes, please open an issue first.
