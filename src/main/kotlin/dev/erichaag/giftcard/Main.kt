package dev.erichaag.giftcard

import com.eatthepath.otp.TimeBasedOneTimePasswordGenerator
import com.google.common.io.BaseEncoding
import com.microsoft.playwright.BrowserType.LaunchOptions
import com.microsoft.playwright.Page
import com.microsoft.playwright.Playwright
import java.time.Instant
import javax.crypto.spec.SecretKeySpec

val authenticatorKey = getenv("AUTHENTICATOR_KEY").filterNot { it.isWhitespace() }
val creditCardLast4 = getenv("CREDIT_CARD_LAST4")
val creditCardName = getenv("CREDIT_CARD_NAME")
val email = getenv("EMAIL")
val giftCardAmount = getenv("GIFT_CARD_AMOUNT")
val giftCardCount = getenv("GIFT_CARD_COUNT").toInt()
val password = getenv("PASSWORD")

const val orderHistoryUrl = "https://www.amazon.com/gp/your-account/order-history"
const val giftCardProductUrl = "https://www.amazon.com/gp/product/B086KKT3RX"

fun main() {
  Playwright.create().use { playwright ->
    val launchOptions = LaunchOptions().setHeadless(false).setSlowMo(50.0)
    val browser = playwright.chromium().launch(launchOptions)
    val page = browser.newPage()

    println("Navigating to $orderHistoryUrl")
    page.navigate(orderHistoryUrl)

    println("Entering email address")
    page.type("#ap_email", email)
    page.click("#continue")

    println("Entering password")
    page.type("#ap_password", password)
    page.click("#signInSubmit")

    println("Entering time-based one time password")
    page.type("#auth-mfa-otpcode", getMfaCode())
    page.click("#auth-signin-button")

    page.waitForSelector("""#yourOrdersContent :text("Your Orders")""")
    println("Successfully navigated to $orderHistoryUrl")

    for (i in 1..giftCardCount) {
      println("--------------------")
      println("Ordering gift card $i/$giftCardCount")
      orderGiftCard(page)
    }
  }
}

fun orderGiftCard(page: Page) {
  println("Navigating to $giftCardProductUrl")
  page.navigate(giftCardProductUrl)

  page.waitForSelector("""#title :text("Amazon Reload")""")
  println("Verified product title is Amazon Reload")

  println("Entering gift card amount of $giftCardAmount")
  page.type("#gcui-asv-reload-form-custom-amount", giftCardAmount)

  println("Clicking 'Buy Now'")
  page.click("#gcui-asv-reload-buynow-button > span > input")

  if (page.waitForSelector("#credit-card-name").textContent().trim() != creditCardName) {
    println("Wrong credit card selected")
    page.click("#payChangeButtonId")

    println("Changing selected credit card to $creditCardName")
    page.waitForSelector("""#apx-content :text("$creditCardName")""").click()
    page.click(".pmts-button-input > span > input")
  }

  page.waitForSelector("""#payment-information :text("$creditCardLast4")""")
  println("Verified credit card ends in $creditCardLast4")

  val actualTotalOrder = page.waitForSelector("#subtotals-marketplace-table .grand-total-price")
    .textContent()
    .trim()
    .replaceFirstChar { if (it == '$') "" else it.toString() }
  if (actualTotalOrder != giftCardAmount) {
    throw RuntimeException("Order total of $actualTotalOrder does not match expected $giftCardAmount")
  }
  println("Verified order total of $actualTotalOrder matches expected $giftCardAmount")

  println("Ordering gift card in 5 seconds...")
  page.waitForTimeout(5000.0)

  println("Ordering gift card now")
  page.click("#submitOrderButtonId > span > input")

  page.waitForSelector("""#widget-purchaseConfirmationStatus :text("Order placed, thanks!")""")
  println("Gift card successfully ordered")
  page.waitForTimeout(2000.0)
}

fun getMfaCode(): String {
  val totp = TimeBasedOneTimePasswordGenerator()
  val secret = SecretKeySpec(BaseEncoding.base32().decode(authenticatorKey), totp.algorithm)
  return totp.generateOneTimePasswordString(secret, Instant.now())
}

fun getenv(name: String): String {
  return System.getenv(name) ?: throw IllegalStateException("Environment variable '$name' is required")
}
