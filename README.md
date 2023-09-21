# # PhishingOut - Combating SMS Phishing in Singapore (Final Year Project)

**PhishingOut** is a community-driven Android app aimed at addressing the pervasive SMS phishing problem in Singapore. This platform provides a robust and intuitive solution for users to verify whether any suspicious SMS messages they receive have malicious intent. By leveraging community participation, PhishingOut creates a collaborative space where members of the public can proactively identify and report potential phishing scams.

![PhishingOut Logo](https://github.com/haikelthaqif/PhishingOut-Android-App/blob/master/miscellaneous/smishingicon.png?raw=true)

## Motivation

The primary inspiration for **PhishingOut** came from witnessing the alarming rise in the number of SMS phishing scam victims in Singapore. According to crime statistics in 2021 by the Singapore Police Force, the total number of reported SMS phishing scam cases was 5,020, marking a 153% increase from the previous year. Scammers managed to steal close to $12.2 million from their victims during this period. SMS phishing scams are more prevalent due to their simplicity in tricking victims by exploiting vulnerability whenever there is doubt.

SMS phishing scams involve sending messages that appear to be from banks or other reputable organizations, often using Caller ID spoofing and fake websites to deceive victims into divulging sensitive information such as login credentials. To combat this growing threat, **PhishingOut** seeks to empower the public to spot SMS phishing messages in advance, preventing them from becoming victims.

## Project Objectives

- **Research and Analysis:** Conduct in-depth research on SMS phishing scams in Singapore and their impacts.
- **Data Collection:** Collate and analyze data from surveys to aid in the feasibility, development, and evaluation of the application.
- **Application Development:** Design and develop a mobile application that enables users to validate suspicious SMS messages and educates them on detecting SMS phishing messages effectively.

## Development Tools

**PhishingOut** is built using the following tools and technologies:

- **Java and Kotlin:** These are the primary programming languages used for Android app development.
- **Android Studio:** Google's Integrated Development Environment (IDE) designed for Android app development.
- **APIs:** Utilizes a WhoIs Lookup API to validate web links and provide relevant information to users.
- **Google Firebase Database:** The database for storing necessary data and user information.

## App Features

### User Authentication
- Users are prompted to log in or register a new account to access the app's features.
**********************************************************************************************************************************************************************************************

### Home Page
- Upon successful login, users are redirected to the Home Page, featuring a bottom navigation menu.
  
![Screenshot of the Home Page](https://github.com/haikelthaqif/PhishingOut-Android-App/blob/master/miscellaneous/Home%20Page.png?raw=true)
**********************************************************************************************************************************************************************************************

### Weblink Validator
- Users can input a web link, and the app initiates the validation process, displaying information regarding the link's legitimacy.
  
![Screenshot of the Weblink Validator](https://github.com/haikelthaqif/PhishingOut-Android-App/blob/master/miscellaneous/WeblinkValidator.png?raw=true)
**********************************************************************************************************************************************************************************************

### Library
- Allows users to browse a library of known SMS phishing messages, categorized by organizations. Users can select or search for specific organizations and view relevant messages.

![Screenshot of the Library](https://github.com/haikelthaqif/PhishingOut-Android-App/blob/master/miscellaneous/Library.png?raw=true)
**********************************************************************************************************************************************************************************************

### Academy
- Offers educational content to enhance users' awareness of SMS phishing scams and how to identify them.

![Screenshot of the Academy](https://github.com/haikelthaqif/PhishingOut-Android-App/blob/master/miscellaneous/Academy.png?raw=true)
**********************************************************************************************************************************************************************************************

### Report
- Guides users through the process of reporting a new phishing message. Users can enter details such as the claimed organization, sender's number, message contents, and provide a screenshot.

![Screenshot of the Report](https://github.com/haikelthaqif/PhishingOut-Android-App/blob/master/miscellaneous/Report.png?raw=true)
**********************************************************************************************************************************************************************************************

