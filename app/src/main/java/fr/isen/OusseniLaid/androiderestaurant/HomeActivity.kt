
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import fr.isen.OusseniLaid.androiderestaurant.R
import fr.isen.OusseniLaid.androiderestaurant.ui.theme.ProjectAndroidTheme
import org.json.JSONObject

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProjectAndroidTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    //color = MaterialTheme.colors.background
                ) {
                    HomePage()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("MainActivity", "L'activité Home est détruite")
    }
}

@Composable
fun HomePage() {
    Surface(
        modifier = Modifier.fillMaxSize(),
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            Image(
                painter = painterResource(id = R.drawable.khitchen),
                contentDescription = "Contact profile picture",
                modifier = Modifier.size(300.dp)
            )

            // Titre en haut à gauche
            Text(
                text = "Bienvenue chez \n\nK'HITCHen",
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 16.dp)
            )

            // Espacement entre le titre et les catégories
            Spacer(modifier = Modifier.height(16.dp))

            // Catégories en colonne avec séparation
            CategoryItem(category = "Entrée")
            Spacer(modifier = Modifier.height(6.dp))
            CategoryItem(category = "Plats")
            Spacer(modifier = Modifier.height(6.dp))
            CategoryItem(category = "Dessert")
        }
    }
}

@Composable
fun CategoryItem(category: String) {
    val context = LocalContext.current
    Surface(
        modifier = Modifier.fillMaxWidth().clickable {
            // Récupération des éléments depuis le serveur
            fetchMenuItems(context, category)
        },
        border = BorderStroke(1.dp, Color.Gray),
        contentColor = Color.Red
    ) {
        Text(
            text = category,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(8.dp)
        )
    }
}


fun fetchMenuItems(context: Context, category: String) {
    val queue = Volley.newRequestQueue(context)
    val url = "http://test.api.catering.bluecodegames.com/menu"

    val jsonObject = JSONObject()
    jsonObject.put("id_shop", "1")

    val request = JsonObjectRequest(Request.Method.POST, url, jsonObject,
        { response ->
            // Traitement de la réponse
            val itemList = response.getJSONArray(category)
            val items = mutableListOf<MenuItem>()
            for (i in 0 until itemList.length()) {
                val itemJson = itemList.getJSONObject(i)
                //val menuItem = gson.fromJson(itemJson.toString(), MenuItem::class.java)
                //items.add(menuItem)
            }
            // Redirection vers la page de catégorie avec les éléments récupérés
            val intent = Intent(context, CategoryActivity::class.java).apply {
                putExtra("items", ArrayList(items))
                putExtra("categoryName", category)
            }
            context.startActivity(intent)
        },
        { error ->
            // Gestion des erreurs
            Toast.makeText(context, "Erreur de récupération des éléments: $error", Toast.LENGTH_SHORT).show()
        })

    queue.add(request)
}

data class MenuItem(
    val title: String,
    val imageUrl: String,
    val price: String
)

class CategoryActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val categoryName = intent.getStringExtra("categoryName")
        val items = intent.getSerializableExtra("items") as? List<MenuItem>

        setContent {
            ProjectAndroidTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    //CategoryPage(categoryName ?: "", items ?: emptyList())
                }
            }
        }
    }
}
