# Gift Card

Uses [Playwright for Java](https://playwright.dev/java/) to automate ordering Amazon Gift Cards.

## Why?

My bank requires me to make a certain number of transactions every month. In return, I receive money back in dividends.

To meet this requirement I order [Amazon Gift Cards](https://www.amazon.com/gp/product/B086KKT3RX). Doing this manually is very tedious and error-prone. In the past I have accidentally ordered too many gift cards, ordered using the wrong card, or ordered for the wrong amount.

This program fully automates the process and verifies everything along the way.

## Environment Variables

In order to run this, the following environment variables must be defined:

| Name                | Description                                                                                                                                                                                                                                                                                                                                                                         |
|---------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `AUTHENTICATOR_KEY` | Your Amazon authenticator key used to generate one-time passwords.<p><b>Note:</b> This is not a one-time password, but rather a key used to generate one-time passwords.<p>You can fetch this value from the [Add a second 2SV authenticator](https://www.amazon.com/a/settings/approval/appbackup?ref=ch_adsec_addExtraApp_attempt) page and clicking on "Can't scan the barcode?" |
| `CREDIT_CARD_LAST4` | The last 4 digits of your credit card number, used for verification purposes.                                                                                                                                                                                                                                                                                                       |
| `CREDIT_CARD_NAME`  | The name of your credit card as defined by your [Amazon wallet](https://www.amazon.com/cpe/yourpayments/wallet).                                                                                                                                                                                                                                                                    |
| `EMAIL`             | The email address you use to log-in to Amazon.                                                                                                                                                                                                                                                                                                                                      |
| `GIFT_CARD_AMOUNT`  | The dollar amount of each gift card.                                                                                                                                                                                                                                                                                                                                                |
| `GIFT_CARD_COUNT`   | The total number of gift cards to purchase.                                                                                                                                                                                                                                                                                                                                         |
| `PASSWORD`          | The password you use to log-in to Amazon.                                                                                                                                                                                                                                                                                                                                           |

## Running

Once all environment variables are defined, the easiest way to run is using the Gradle wrapper.

```shell
$ ./gradlew run
```
