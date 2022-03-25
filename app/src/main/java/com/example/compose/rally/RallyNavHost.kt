package com.example.compose.rally

import android.accounts.Account
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.example.compose.rally.data.UserData
import com.example.compose.rally.ui.accounts.AccountsBody
import com.example.compose.rally.ui.accounts.SingleAccountBody
import com.example.compose.rally.ui.bills.BillsBody
import com.example.compose.rally.ui.overview.OverviewBody

val accountsName = RallyScreen.Accounts.name

@Composable
fun RallyNavHost(
    navController: NavHostController,
    innerPadding: PaddingValues
) {

    NavHost(
        navController = navController,
        startDestination = RallyScreen.Overview.name,
        modifier = Modifier.padding(innerPadding)
    ) {
        composable(route = RallyScreen.Overview.name) {
            OverviewBody(
                onClickSeeAllAccounts = { navController.navigate(RallyScreen.Accounts.name) },
                onClickSeeAllBills = { navController.navigate(RallyScreen.Bills.name) },
                onAccountClick = { name -> navigateToSingleAccount(navController, name) }
            )
        }
        composable(route = RallyScreen.Accounts.name) {
            AccountsBody(accounts = UserData.accounts) { name ->
                navigateToSingleAccount(
                    navController = navController,
                    accountName = name
                )
            }
        }
        composable(route = RallyScreen.Bills.name) {
            BillsBody(bills = UserData.bills)
        }
        composable(
            route = "$accountsName/{name}",
            arguments = listOf(navArgument("name") {
                type = NavType.StringType
            }),
            deepLinks = listOf(navDeepLink {
                uriPattern = "rally://$accountsName/{name}"
            })
        ) { backStackEntry ->
            val accountName = backStackEntry.arguments?.getString("name")
            val account = UserData.getAccount(accountName)
            SingleAccountBody(account = account)
        }

    }
}

fun navigateToSingleAccount(
    navController: NavHostController,
    accountName: String
) = navController.navigate("${accountsName}/$accountName")