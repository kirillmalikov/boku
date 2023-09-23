# boku Test Assignment

To expand and improve our coverage in the Middle East, Boku is integrating a new payment solutions provider in Bahrain. 
It will be a traditional Premium-SMSconnection, which means that payments will be made by sending a text message (SMS) to a premium rated short number. 
Users will receive the content they paid forin the reply message (e.g. a link or a code to unlock some content).

- **Provider**
receives the SMS from the user and makes the charge. After that notifies Boku about the payment.
- **Merchant** 
receives notification from Boku about the payment. Responds with the content, which will be sent back to the user in the reply message.
- **Boku** 
provides the platform connecting payment providers and service providers. Forwards payment notifications to merchants and sends reply messages to theusers.
- **User**
sends a premium rate SMS to provider's short number to initiate the payment. Receives a reply SMS with the ordered content afterwards.
  

Taking the role of "Boku", your task is to create a simple application, that has 3 main responsibilities:
1. receive the payment notification from our provider
2. forward the notification to our merchant in order to receive content for the reply message
3. send the reply message via our provider

### Used technologies
* Java 17
* SpringBoot
* JUnit
* Mockito
* Gradle

### Running the project

1. Download .zip or clone project from GitHub.

2. Build the project using gradle
   ```sh
   ./gradlew build
   ```

3. Run the project
   ```sh
   ./gradlew bootRun
   ```

4. The application runs on 8080 port

### How to use


 | Endpoint (GET)        |     
 |-----------------------|
 | localhost:8080/v1/sms |


| Query Parameters |                                   |
|------------------|-----------------------------------|
| message_id       | String                            | 
| operator         | String                            |
| receiver         | Digits                            |
| sender           | String (_+32755554444_)           |
| text             | String (_starts with TXT \| FOR_) |
| timestamp        | String (_yyyy-MM-dd HH.mm.ss_)    |
