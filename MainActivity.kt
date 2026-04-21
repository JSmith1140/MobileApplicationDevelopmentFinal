package com.universityXYZ.rogerwilliamsuniversity

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.universityXYZ.rogerwilliamsuniversity.ui.theme.CourseTheme

private val validGrades = setOf(
    "A", "A-",
    "B+", "B", "B-",
    "C+", "C", "C-",
    "D+", "D", "D-",
    "F"
)

data class BoxItem(val title: String, val detail: String, val imageRes: Int? = null)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CourseTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val owner = LocalViewModelStoreOwner.current
                    owner?.let {
                        val viewModel: MainViewModel = viewModel(
                            it,
                            "MainViewModel",
                            MainViewModelFactory(
                                LocalContext.current.applicationContext as Application
                            )
                        )
                        TabScreen(viewModel)
                    }
                }
            }
        }
    }
}

@Composable
fun TabScreen(viewModel: MainViewModel) {
    var tabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf("Acad", "Trivial", "Home", "Dining", "Settings")

    Column(modifier = Modifier.fillMaxSize()) {

        Box(modifier = Modifier.weight(1f)) {
            when (tabIndex) {
                0 -> ScreenSetup(viewModel)
                1 -> TrivialScreen(viewModel)
                2 -> HomeScreen()
                3 -> DiningScreen()
                4 -> SettingsScreen()
            }
        }

        TabRow(
            selectedTabIndex = tabIndex,
            containerColor = MaterialTheme.colorScheme.secondary,
            contentColor = MaterialTheme.colorScheme.onSecondary
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = tabIndex == index,
                    onClick = { tabIndex = index },
                    text = {
                        Text(
                            text = title,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            softWrap = false
                        )
                    },
                    selectedContentColor = MaterialTheme.colorScheme.tertiary,
                    unselectedContentColor = MaterialTheme.colorScheme.onSecondary
                )
            }
        }
    }
}

@Composable
fun HistoryTimeline() {
    val historyEvents = listOf(
        BoxItem("1919", "Origin: Northeastern University opens a branch in Providence, laying the groundwork for the future RWU."),
        BoxItem("1945", "Expansion: Post-WWII enrollment surge brings new focus on business and technical programs."),
        BoxItem("1956", "Independence: The school is officially chartered as Roger Williams Junior College."),
        BoxItem("1969", "The Big Move: RWU relocates to the beautiful 140-acre waterfront campus in Bristol, RI."),
        BoxItem("1992", "University Status: The institution is renamed Roger Williams University, reflecting its academic growth."),
        BoxItem("2012", "Legal Excellence: RWU Law is recognized as a top school for public interest law in the region."),
        BoxItem("2024", "Modern Era: RWU leads in coastal sustainability and community-engaged learning.")
    )

    var currentIndex by remember { mutableIntStateOf(4) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        BoxHeader("University History")

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F8FF)),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = { if (currentIndex > 0) currentIndex-- },
                    enabled = currentIndex > 0
                ) {
                    Icon(
                        painter = painterResource(id = android.R.drawable.ic_media_previous),
                        contentDescription = "Earlier",
                        tint = if (currentIndex > 0) MaterialTheme.colorScheme.secondary else Color.LightGray
                    )
                }

                Column(
                    modifier = Modifier.weight(1f).padding(horizontal = 12.dp),
                ) {
                    Text(
                        text = historyEvents[currentIndex].title,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 22.sp,
                        color = MaterialTheme.colorScheme.tertiary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = historyEvents[currentIndex].detail,
                        textAlign = TextAlign.Center,
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.secondary,
                        lineHeight = 20.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Page ${currentIndex + 1} of ${historyEvents.size}",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }

                IconButton(
                    onClick = { if (currentIndex < historyEvents.size - 1) currentIndex++ },
                    enabled = currentIndex < historyEvents.size - 1
                ) {
                    Icon(
                        painter = painterResource(id = android.R.drawable.ic_media_next),
                        contentDescription = "Later",
                        tint = if (currentIndex < historyEvents.size - 1) MaterialTheme.colorScheme.secondary else Color.LightGray
                    )
                }
            }
        }
    }
}

@Composable
fun HomeScreen() {
    val nationalRankings = listOf(
        BoxItem("Best Regional University", "Roger Williams University is placed 35th as the best regional university in the North. This ranking highlights our commitment to providing a top-tier education and a supportive learning environment for all students.", R.drawable.rwuimage),
        BoxItem("Best Schools for Veterans", "Ranked 47th for best schools for veterans. We take great pride in supporting our service members with dedicated resources, flexible programs, and a welcoming community that honors their service.", R.drawable.rwuveterans),
        BoxItem("Architecture Excellence", "The Architecture program is ranked 78th in the nation. Our Cummings School of Architecture is renowned for its innovative curriculum and for preparing students to lead in a rapidly changing world.", R.drawable.rwuarchitecture),
        BoxItem("Best Campus Food", "Our food is ranked 21st in the nation! From locally sourced ingredients to diverse culinary options, our dining services consistently deliver a high-quality experience for the entire campus community.", R.drawable.rwucampusfood)
    )

    val athleticAchievements = listOf(
        BoxItem("CCC Team Champions", "Mens track & field 2024 CCC team Champions scoring 252 points. This victory showcases the incredible talent and dedication of our student-athletes and coaching staff in achieving excellence on the field.", R.drawable.rwutrack),
        BoxItem("First 2,000-Point Scorer", "Mens basketball player Ian Coene became the first 2,000-point scorer in program history. This historic milestone reflects years of hard work, skill, and a passion for the game that inspires us all.", R.drawable.rwubasketball),
        BoxItem("Records Broken", "Woman's basketball player Katy Bovee set records with 1,570 career points and 178 three-pointers. Her outstanding performance on the court has cemented her legacy as one of the program's all-time greats.", R.drawable.rwuwomenbasketball),
        BoxItem("National Sailing Champions", "Sailing team won the 2024 ICSA Open Team Race National Championship. This achievement solidifies Roger Williams University's position as a powerhouse in collegiate sailing on a national stage.", R.drawable.rwu_sailing)
    )

    val alumni = listOf(
        BoxItem("Tim Baxter", "Tim Baxter served as the CEO of Samsung Electronics North America. His leadership in the global tech industry is a testament to the strong foundation and leadership skills developed during his time at RWU.", R.drawable.tim_baxter),
        BoxItem("Chris Sparling", "Chris Sparling is a successful screenwriter and director, known for his work on films like 'Buried'. His creative contributions to the film industry bring great pride to his alma mater.", R.drawable.chris_sparling),
        BoxItem("Jerry Remy", "Jerry Remy was a former MLB player and a beloved broadcaster for the Boston Red Sox. Known as 'RemDawg', his legacy in the world of sports and media continues to inspire fans and students alike.", R.drawable.jerry_remy),
        BoxItem("James W. Nuttall", "James W. Nuttall is a retired United States Army Major General. His distinguished career in military service exemplifies the values of leadership, duty, and honor that we strive to instill in all RWU students.", R.drawable.james_nuttall),
        BoxItem("Joe Polisena", "Joe Polisena is a former Rhode Island State Senator and served as the mayor of Johnston, RI. His dedication to public service and community development has had a lasting impact on the region.", R.drawable.joe_polesina),
        BoxItem("Peter Kilmartin", "Peter Kilmartin served as the 73rd Attorney General of Rhode Island. His commitment to justice and public safety throughout his career reflects the high standards of our alumni in the legal profession.", R.drawable.peter_kilmartin1)
    )

    val braggingRights = listOf(
        BoxItem("Beautiful Bristol Campus", "Stunning waterfront views on our historic 140-acre campus. Our location in Bristol, Rhode Island, provides an inspiring backdrop for learning, living, and creating lifelong memories.", R.drawable.rwucampuspic),
        BoxItem("Global Sustainability", "Top 5% of universities globally for sustainability initiatives. We are committed to environmental stewardship and integrating sustainable practices into every aspect of campus life.", R.drawable.rwuclimate),
        BoxItem("Student Engagement", "Ranked 1st in Rhode Island for student engagement and success. Our faculty and staff are dedicated to ensuring every student has the support they need to thrive academically and personally.", R.drawable.rwustudentengagment)
    )

    Column(modifier = Modifier.fillMaxSize()) {
        HeaderSection("Home")

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 20.dp)
        ) {
            item { Spacer(modifier = Modifier.height(8.dp)) }

            item { HistoryTimeline() }

            item { BoxHeader("National Rankings") }
            items(nationalRankings) { item -> ExpandableBoxCard(item) }

            item { BoxHeader("Athletic Achievements") }
            items(athleticAchievements) { item -> ExpandableBoxCard(item) }

            item { BoxHeader("Notable Alumni & People") }
            items(alumni) { item -> ExpandableBoxCard(item) }

            item { BoxHeader("More Bragging Rights") }
            items(braggingRights) { item -> ExpandableBoxCard(item) }
        }
    }
}

@Composable
fun BoxHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.headlineSmall,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.secondary,
        modifier = Modifier.padding(vertical = 8.dp)
    )
}

@Composable
fun BoxScope.SparkleEffect() {
    val infiniteTransition = rememberInfiniteTransition(label = "sparkle")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "alpha"
    )
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.7f,
        targetValue = 1.3f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "scale"
    )

    Canvas(modifier = Modifier.matchParentSize()) {
        val random = java.util.Random(123)
        repeat(15) {
            val x = random.nextFloat() * size.width
            val y = random.nextFloat() * size.height
            val radius = (random.nextFloat() * 2 + 2).dp.toPx() * scale
            drawCircle(
                color = Color.White.copy(alpha = alpha * 0.8f),
                radius = radius,
                center = Offset(x, y)
            )
        }
    }
}

@Composable
fun ExpandableBoxCard(item: BoxItem) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded }
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioLowBouncy,
                    stiffness = Spring.StiffnessLow
                )
            ),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFE6E6FA)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            if (expanded) {
                SparkleEffect()
            }
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = item.title,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.secondary,
                        fontSize = 16.sp,
                        modifier = Modifier.weight(1f)
                    )
                    Icon(
                        painter = painterResource(
                            id = if (expanded) android.R.drawable.arrow_up_float else android.R.drawable.arrow_down_float
                        ),
                        contentDescription = if (expanded) "Collapse" else "Expand",
                        tint = MaterialTheme.colorScheme.tertiary
                    )
                }
                if (expanded) {
                    Spacer(modifier = Modifier.height(8.dp))

                    if (item.imageRes != null) {
                        Image(
                            painter = painterResource(id = item.imageRes),
                            contentDescription = item.title,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(180.dp)
                                .padding(bottom = 8.dp),
                            contentScale = ContentScale.Fit
                        )
                    }

                    Text(
                        text = item.detail,
                        color = MaterialTheme.colorScheme.secondary,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

@Composable
fun DiningScreen() {

    val diningHalls = listOf(
        BoxItem("Upper Commons", "The Upper Commons is the University’s main dining hall serving breakfast, lunch, and dinner. Choices seem endless at this all-you-care-to-eat café — from made-to-order stir-fry, fresh salads, comforting entrée’s, to tempting pizza, pasta, and grill favorites. The Upper Commons also has a dedicated made-without-gluten station and made-without-gluten options at all stations.", R.drawable.uppercommons2),
        BoxItem("Lower Commons", "The Lower Commons is located right below the Upper Commons and has everything from GO meals to sushi, pizza, grill, a salad bar, and made-to-order sandwiches – fire grilled or cold!", R.drawable.lowercommons),
        BoxItem("Roger's Café", "The Roger's Café is a taqueria-inspired café serving breakfast and lunch along with GO meals, soup, and sushi.", R.drawable.cafe),
        BoxItem("Hawk's Nest", "The Hawk’s Nest is located in the Recreation Center and serves coffee, fresh pastries, and bagels in addition to fruit smoothies and GO meals.", R.drawable.hawksnest),
        BoxItem("Global Café", "Global Café serves Starbucks coffee and specializes in espresso drinks, but also GO meals, grilled sandwiches, and breakfast sandwiches.", R.drawable.globalcafe),
        BoxItem("Just Baked", "Just Baked kiosk is located in Center for Student Development offers 24hr convenient hot meal and snacks.", R.drawable.justbaked)
    )

    val mealPlans = listOf(
        BoxItem("125 Block Plan", "Ideal plan for apartment dwellers who want to eat an average of 7-8 meals per week on campus and use 400 in Hawk Dollars throughout the semester; 3 Bonus Meals per semester to be used for yourself, another on-campus student, or commuter."),
        BoxItem("Block Meal Plans", "Block Meal Plans offer students the most flexibility as the amount of meals can be used throughout the semester in any manner. Students can use their block meals at our two all-you-care-to-eat dining halls.  All Block Meal Plans come with Bonus Meals, these meals can be used for guests not on a Roger Williams dining plan."),
        BoxItem("Commuter Plan", "This plan offers the best value for commuting students. You will receive 200 meals per semester that can be eaten at any meal period, as well as 300 Hawk Dollars."),
        BoxItem("Hawk Dollars", "Hawk Dollars are accepted like cash in all our dining locations.  Hawk Dollars are a “declining balance account” that works on the same principle as a debit card.  You can use your Hawk Dollars to purchase beverages, snacks, or even a full meal in all of the RWU retail and residential locations."),
        BoxItem("Points", "Much like Hawk Dollars, Points are accepted like cash in all our dining locations and are a “declining balance account” that works on the same principle as a debit card ")
    )

    Column(modifier = Modifier.fillMaxSize()) {
        HeaderSection("Dining Services")

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 20.dp)
        ) {
            item { Spacer(modifier = Modifier.height(8.dp)) }

            item { BoxHeader("Dining Locations") }
            items(diningHalls) { hall ->
                ExpandableBoxCard(hall)
            }

            item { BoxHeader("Meal Plan Options") }
            items(mealPlans) { plan ->
                ExpandableBoxCard(plan)
            }
        }
    }
}

@Composable
fun SettingsScreen() {
    Column(modifier = Modifier.fillMaxSize()) {
        HeaderSection("Settings")

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 20.dp)
        ) {
            item { Spacer(modifier = Modifier.height(8.dp)) }

            item { BoxHeader("App Credits") }
            item { CreditItem("Group Members", "Jacob Smith, Adam Walton, Anton Ryan, Nathan Valle") }
            item { CreditItem("Version", "3.2.4") }

            item { BoxHeader("Campus Life") }
            item { CreditItem("Meal Swipes Left", "142") }
            item { CreditItem("Hawk Dollars", "$250.00") }

            item { BoxHeader("Fun Fact") }
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE6E6FA)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Text(
                        text = "Roger Williams University played Merrimack College in rugby twice this season, and both Adam and Anton play for Merrimack. We lost both games :(",
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.secondary,
                        lineHeight = 22.sp
                    )
                }
            }
        }
    }
}

@Composable
fun CreditItem(label: String, value: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F8FF)),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(14.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = label,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.secondary,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                color = MaterialTheme.colorScheme.tertiary,
                fontSize = 15.sp,
                lineHeight = 20.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
fun TrivialScreen(viewModel: MainViewModel) {
    val allQuestions by viewModel.allTrivialQuestions.observeAsState(listOf())
    var numberOfQuestions by remember { mutableStateOf("") }
    var questions by remember { mutableStateOf<List<TrivialQuestion>>(emptyList()) }
    val (selectedAnswers, setSelectedAnswers) = remember { mutableStateOf<Map<Int, String>>(emptyMap()) }
    var score by remember { mutableStateOf<String?>(null) }
    var isError by remember { mutableStateOf(false) }
    var waitingForLoad by remember { mutableStateOf(false) }

    LaunchedEffect(allQuestions) {
        if (waitingForLoad && allQuestions.isNotEmpty()) {
            questions = allQuestions.shuffled()
        }
    }

    val navyButtonColors = ButtonDefaults.buttonColors(
        containerColor = MaterialTheme.colorScheme.secondary,
        contentColor = MaterialTheme.colorScheme.onSecondary
    )
    val orangeButtonColors = ButtonDefaults.buttonColors(
        containerColor = MaterialTheme.colorScheme.tertiary,
        contentColor = MaterialTheme.colorScheme.onTertiary
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 8.dp),
        horizontalAlignment = CenterHorizontally
    ) {
        HeaderSection("University Trivia")

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                viewModel.loadTriviaQuestions()
                setSelectedAnswers(emptyMap())
                score = null
                isError = false

                if (allQuestions.isNotEmpty()) {
                    questions = allQuestions.shuffled()
                } else {
                    waitingForLoad = true
                }
            },
            colors = navyButtonColors,
            modifier = Modifier.width(100.dp)
        ) {
            Text("Load", fontSize = 16.sp)
        }

        CustomTextField(
            title = "Number of Questions",
            textState = numberOfQuestions,
            onTextChange = {
                numberOfQuestions = it
                isError = false
            },
            keyboardType = KeyboardType.Number,
            isError = isError,
            errorMessage = "Enter 1 to 10"
        )

        Button(
            onClick = {
                val num = numberOfQuestions.toIntOrNull()
                if (num != null && num > 0 && num <= 10) {
                    isError = false
                    if (allQuestions.isNotEmpty()) {
                        questions = allQuestions.shuffled().take(num)
                    } else {
                        viewModel.loadTriviaQuestions()
                        waitingForLoad = true
                    }
                    setSelectedAnswers(emptyMap())
                    score = null
                } else {
                    isError = true
                }
            },
            colors = orangeButtonColors,
            modifier = Modifier.width(80.dp)
        ) {
            Text("Go", fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(questions) { question ->
                QuestionCard(
                    question = question,
                    selectedAnswer = selectedAnswers[question.id],
                    onAnswerSelected = {
                        val newAnswers = selectedAnswers.toMutableMap()
                        newAnswers[question.id] = it
                        setSelectedAnswers(newAnswers)
                    }
                )
            }
        }

        if (questions.isNotEmpty()) {
            Button(
                onClick = {
                    var correct = 0
                    questions.forEach { question ->
                        if (selectedAnswers[question.id] == question.correctAnswer) {
                            correct++
                        }
                    }
                    score = "$correct/${questions.size}"
                },
                enabled = selectedAnswers.size == questions.size,
                colors = orangeButtonColors,
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .width(110.dp)
            ) {
                Text("Grade", fontSize = 16.sp)
            }
        }

        score?.let {
            Column(
                horizontalAlignment = CenterHorizontally,
                modifier = Modifier.padding(bottom = 12.dp)
            ) {
                Text(
                    text = "Your Score",
                    color = MaterialTheme.colorScheme.tertiary,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.secondary,
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.ExtraBold
                )
            }
        }
    }
}

@Composable
fun QuestionCard(question: TrivialQuestion, selectedAnswer: String?, onAnswerSelected: (String) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFE6E6FA)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = question.questionName,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.secondary,
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(8.dp))

            val choices = listOf("A", "B", "C", "D")
            val choiceText = listOf(question.choiceA, question.choiceB, question.choiceC, question.choiceD)

            choices.forEachIndexed { index, choice ->
                Row(
                    Modifier
                        .fillMaxWidth()
                        .selectable(
                            selected = (selectedAnswer == choice),
                            onClick = { onAnswerSelected(choice) } )
                        .padding(vertical = 2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = (selectedAnswer == choice),
                        onClick = { onAnswerSelected(choice) },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = MaterialTheme.colorScheme.tertiary,
                            unselectedColor = MaterialTheme.colorScheme.secondary
                        )
                    )
                    Text(
                        text = "$choice. ${choiceText[index]}",
                        color = MaterialTheme.colorScheme.secondary,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}


@Composable
fun ScreenSetup(viewModel: MainViewModel) {
    val allCourses by viewModel.allCourses.observeAsState(listOf())
    val searchResults by viewModel.searchResults.observeAsState(listOf())

    MainScreen(
        allCourses = allCourses,
        searchResults = searchResults,
        viewModel = viewModel
    )
}

@Composable
fun MainScreen(
    allCourses: List<Course>,
    searchResults: List<Course>,
    viewModel: MainViewModel
) {
    var courseName by remember { mutableStateOf("") }
    var courseCreditHour by remember { mutableStateOf("") }
    var letterGrade by remember { mutableStateOf("") }

    var calculatedGPA by remember { mutableDoubleStateOf(-1.0) }
    var searching by remember { mutableStateOf(false) }

    val isGradeValid = letterGrade.isNotEmpty() &&
            letterGrade.uppercase() in validGrades

    val isCreditHourValid = courseCreditHour.isNotEmpty() &&
            courseCreditHour.toIntOrNull() != null

    val navyButtonColors = ButtonDefaults.buttonColors(
        containerColor = MaterialTheme.colorScheme.secondary,
        contentColor = MaterialTheme.colorScheme.onSecondary
    )
    val orangeButtonColors = ButtonDefaults.buttonColors(
        containerColor = MaterialTheme.colorScheme.tertiary,
        contentColor = MaterialTheme.colorScheme.onTertiary
    )

    Column(
        horizontalAlignment = CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
    ) {
        HeaderSection("Course GPA Tracker")

        CustomTextField(
            title = "Course Name",
            textState = courseName,
            onTextChange = { courseName = it },
            keyboardType = KeyboardType.Text
        )

        CustomTextField(
            title = "Credit Hour",
            textState = courseCreditHour,
            onTextChange = { courseCreditHour = it },
            keyboardType = KeyboardType.Number,
            isError = courseCreditHour.isNotEmpty() && !isCreditHourValid,
            errorMessage = "Credit Hour must be a number"
        )

        CustomTextField(
            title = "Letter Grade",
            textState = letterGrade,
            onTextChange = { letterGrade = it.trim() },
            keyboardType = KeyboardType.Text,
            isError = letterGrade.isNotEmpty() && !isGradeValid,
            errorMessage = "Valid grades: A, A-, B+, B, B-, C+, C, C-, D+, D, D-, F"
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 6.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                Button(
                    onClick = {
                        if (courseCreditHour.isNotEmpty() && isGradeValid) {
                            viewModel.insertCourse(
                                Course(
                                    courseName,
                                    courseCreditHour.toInt(),
                                    letterGrade.uppercase()
                                )
                            )
                            searching = false
                        }
                    },
                    colors = navyButtonColors,
                    enabled = isGradeValid
                ) { Text("Add") }

                Button(
                    onClick = {
                        searching = true
                        viewModel.findCourse(courseName)
                    },
                    colors = navyButtonColors
                ) { Text("Sch") }

                Button(
                    onClick = {
                        searching = false
                        viewModel.deleteCourse(courseName)
                    },
                    colors = navyButtonColors
                ) { Text("Del") }

                Button(
                    onClick = {
                        searching = false
                        courseName = ""
                        courseCreditHour = ""
                        letterGrade = ""
                    },
                    colors = navyButtonColors
                ) { Text("Clr") }

                Button(
                    onClick = {
                        calculatedGPA = calculateGPA(allCourses)
                    },
                    colors = orangeButtonColors
                ) { Text("GPA") }
            }
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 4.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Current GPA", style = MaterialTheme.typography.titleMedium)
                Text(
                    text = if (calculatedGPA < 0) "--" else "%.2f".format(calculatedGPA),
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.tertiary
                )
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            val list = if (searching) searchResults else allCourses

            item {
                TitleRow("ID", "Course", "Credit", "Grade")
            }

            items(list) { course ->
                CourseRow(
                    id = course.id,
                    name = course.courseName,
                    creditHour = course.creditHour,
                    letterGrade = course.letterGrade
                )
            }
        }
    }
}

@Composable
private fun HeaderSection(title: String) {
    Image(
        painter = painterResource(id = R.drawable.rwulogo),
        contentDescription = "Header Image",
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp),
        contentScale = ContentScale.Fit
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.secondary)
            .padding(horizontal = 14.dp, vertical = 4.dp)
    ) {
        Text(
            text = title,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSecondary,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
    }

    HorizontalDivider(
        color = MaterialTheme.colorScheme.tertiary,
        thickness = 3.dp
    )
}

private fun calculateGPA(courses: List<Course>): Double {
    val gradePoints = mapOf(
        "A" to 4.0, "A-" to 3.67,
        "B+" to 3.33, "B" to 3.0, "B-" to 2.67,
        "C+" to 2.33, "C" to 2.0, "C-" to 1.67,
        "D+" to 1.33, "D" to 1.0, "D-" to 0.67,
        "F" to 0.0
    )

    val totalCreditHours = courses.sumOf { it.creditHour }
    if (totalCreditHours == 0) return 0.0

    val totalPoints = courses.sumOf {
        it.creditHour * (gradePoints[it.letterGrade.uppercase()] ?: 0.0)
    }

    return totalPoints / totalCreditHours
}

@Composable
fun TitleRow(head1: String, head2: String, head3: String, head4: String) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.secondary)
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 6.dp)
        ) {
            val color = MaterialTheme.colorScheme.onSecondary
            Text(head1, modifier = Modifier.weight(0.12f), color = color, fontWeight = FontWeight.Bold)
            Text(head2, modifier = Modifier.weight(0.38f), color = color, fontWeight = FontWeight.Bold)
            Text(head3, modifier = Modifier.weight(0.20f), color = color, fontWeight = FontWeight.Bold)
            Text(head4, modifier = Modifier.weight(0.20f), color = color, fontWeight = FontWeight.Bold)
        }

        HorizontalDivider(color = MaterialTheme.colorScheme.tertiary, thickness = 2.dp)
    }
}

@Composable
fun CourseRow(id: Int, name: String, creditHour: Int, letterGrade: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 10.dp)
        ) {
            Text(id.toString(), modifier = Modifier.weight(0.12f))
            Text(name, modifier = Modifier.weight(0.38f))
            Text(creditHour.toString(), modifier = Modifier.weight(0.20f))
            Text(
                letterGrade,
                modifier = Modifier.weight(0.20f),
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTextField(
    title: String,
    textState: String,
    onTextChange: (String) -> Unit,
    keyboardType: KeyboardType,
    isError: Boolean = false,
    errorMessage: String? = null
) {
    Column {
        OutlinedTextField(
            value = textState,
            onValueChange = onTextChange,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            singleLine = true,
            label = { Text(title) },
            isError = isError,
            modifier = Modifier
                .padding(horizontal = 10.dp, vertical = 4.dp)
                .fillMaxWidth(),
            textStyle = TextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            ),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.tertiary,
                unfocusedBorderColor = MaterialTheme.colorScheme.tertiary,
                focusedLabelColor = MaterialTheme.colorScheme.secondary,
                unfocusedLabelColor = MaterialTheme.colorScheme.secondary,
                cursorColor = MaterialTheme.colorScheme.secondary
            )
        )

        if (isError && errorMessage != null) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 16.dp)
            )
        }
    }
}

class MainViewModelFactory(val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return MainViewModel(application) as T
    }
}
