package com.csci448.backstreet_bowlers.guttertalk.ui.leaderboard.scores

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.csci448.backstreet_bowlers.guttertalk.data.UserInformation


@Composable
fun LeaderboardComposable(users: List<UserInformation>?){
    Column(
        modifier = Modifier
            .padding(1.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Global Leaderboards",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = "Players are ranked by their lifetime score",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            text = "Players",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 5.dp)
        )
        if(users == null){
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp))
                Spacer(Modifier.width(12.dp))
                Text("Loading Leaderboard...")
            }
        }else{
            var usersList = mutableListOf<UserInformation>()
            // Simulated logic of pulling scores
            if (!users.isEmpty()) {
                users.forEach { record ->
                    usersList.add(record)
                }
            }
            usersList.sortByDescending { it.lifetimeScore }
            GlobalLeaderboard(usersList)
        }
    }
}
@Composable
fun GlobalLeaderboard(users: List<UserInformation>) {
    LazyColumn {
        items(users.size) { user ->
            LeaderboardCard(user = users.get(user))
        }
    }
}

@Composable
fun LeaderboardCard(user: UserInformation) {
    // Local state to track if this specific card is expanded
    var isExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp)
            .animateContentSize()
            .clickable { isExpanded = !isExpanded }, // Toggle state on click
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.LightGray)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Basic leaderboard info
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = user.username, style = MaterialTheme.typography.titleLarge)
                    Text(text = user.location ?: "Unknown Location", style = MaterialTheme.typography.bodySmall)
                }
                Text(
                    text = "${user.lifetimeScore}",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            // Extra statistics
            if (isExpanded) {
                Spacer(modifier = Modifier.height(16.dp))
                Divider()
                Spacer(modifier = Modifier.height(16.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                    StatItem(label = "Strikes", value = user.lifetimeStrikes)
                    StatItem(label = "Spares", value = user.lifetimeSpares)
                    StatItem(label = "High Score", value = user.highestScore)
                }
            }
        }
    }
}

@Composable
fun StatItem(label: String, value: Int) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = label, style = MaterialTheme.typography.labelSmall)
        Text(text = value.toString(), fontWeight = FontWeight.Bold)
    }
}

@Preview(showBackground = true, name = "Full Leaderboard")
@Composable
fun PreviewLeaderboard() {
    LeaderboardComposable(users = MockDataGlobal.userList)
}

@Preview(showBackground = true, name = "Buffering Leaderboard")
@Composable
fun PreviewBufferingLeaderboard() {
    LeaderboardComposable(users = null)
}

@Preview(showBackground = true, name = "Single Card Detail")
@Composable
fun PreviewSingleCard() {
    LeaderboardCard(user = MockDataGlobal.user1)
}

object MockDataGlobal {
    val user1 = UserInformation(
        PlayerID = "user_001",
        username = "StrikeMaster",
        gamesPlayed = 45,
        lifetimeScore = 8200,
        lifetimeSpares = 120,
        lifetimeStrikes = 210,
        location = "Golden, CO",
        highestScore = 289
    )

    val user2 = UserInformation(
        PlayerID = "user_002",
        username = "SpareMe",
        gamesPlayed = 30,
        lifetimeScore = 5400,
        lifetimeSpares = 150,
        lifetimeStrikes = 85,
        location = "Denver, CO",
        highestScore = 210
    )

    val userList = listOf(user1, user2,
        user1.copy(username = "Monster Enegy", PlayerID = "user_003"),
        user2.copy(username = "Mantle", PlayerID = "user_003"),
        user1.copy(username = "Danny", PlayerID = "user_003"),
        user2.copy(username = "BRAIN", PlayerID = "user_003"),
        user1.copy(username = "Donk E kong", PlayerID = "user_003"),)
}