Feature: Credentials Controller Features

  Scenario Outline: the client makes a POST request to the login endpoint with correct details and receives expected body and response code
    When Client makes a "POST" request to "login" endpoint with the following body
      | username | <username> |
      | password | <password> |
    Then Client receives a status code of 200
    And The response has the following properties
      | username   | <username>   |
      | role       | <role>       |
      | attempts   | 0            |
      | firstLogin | <firstLogin> |
    Examples:
      | username        | password    | firstLogin | role  |
      | admin           | defaultPass | false      | admin |
      | user            | defaultPass | false      | user  |
      | adminFirstLogin | defaultPass | true       | admin |
      | userFirstLogin  | defaultPass | true       | user  |

  Scenario: the client makes a POST request to the login endpoint with wrong details and receives expected body and response code
    When Client makes a "POST" request to "login" endpoint with the following body
      | username | admin         |
      | password | wrongPassword |
    Then Client receives a status code of 500
    And The response has the following properties
      | description | User error                    |
      | details     | Credenziali non riconosciute. |

  Scenario: the client makes a POST request to the login endpoint with wrong details 5 times their the account gets locked and receives expected body and response code
    When Client makes a "POST" request to "login" endpoint with the following body
      | username | userToLock    |
      | password | wrongPassword |
    And The response has the following properties
      | description | User error                    |
      | details     | Credenziali non riconosciute. |
    When Client makes a "POST" request to "login" endpoint with the following body
      | username | userToLock    |
      | password | wrongPassword |
    And The response has the following properties
      | description | User error                    |
      | details     | Credenziali non riconosciute. |
    When Client makes a "POST" request to "login" endpoint with the following body
      | username | userToLock    |
      | password | wrongPassword |
    And The response has the following properties
      | description | User error                    |
      | details     | Credenziali non riconosciute. |
    When Client makes a "POST" request to "login" endpoint with the following body
      | username | userToLock    |
      | password | wrongPassword |
    And The response has the following properties
      | description | User error                    |
      | details     | Credenziali non riconosciute. |
    When Client makes a "POST" request to "login" endpoint with the following body
      | username | userToLock    |
      | password | wrongPassword |
    And The response has the following properties
      | description | User error                                                  |
      | details     | Account bloccato, contatta l'amministratore per sbloccarlo. |

  Scenario Outline: the client makes a POST request to the login endpoint with correct details but a locked account and receives expected body and response code
    When Client makes a "POST" request to "login" endpoint with the following body
      | username | <username> |
      | password | <password> |
    Then Client receives a status code of 500
    And The response has the following properties
      | description | User error                                                  |
      | details     | Account bloccato, contatta l'amministratore per sbloccarlo. |
    Examples:
      | username    | password    |
      | adminLocked | defaultPass |
      | userLocked  | defaultPass |

  Scenario: the client makes a POST request to the change-password endpoint with wrong details and receives expected response code
    Given Client provides "invalid" "admin" authentication credentials
    When Client makes a "POST" request to "change-password" endpoint with the following body
      | username    | admin         |
      | password    | wrongPassword |
      | newPassword | newPassword   |
    Then Client receives a status code of 401

  Scenario Outline: the client makes a POST request to the change-password endpoint with correct details and a new password that is not strong and receives expected body and response code
    Given Client provides "valid" "userPassChange" authentication credentials
    When Client makes a "POST" request to "change-password" endpoint with the following body
      | username    | userPassChange |
      | password    | defaultPass    |
      | newPassword | <newPassword>  |
    Then Client receives a status code of 500
    And The response has the following properties
      | description | User error                                                                                             |
      | details     | La nuova password ha una sicurezza di tipo: '<passStrength>'. Riprova e assicurati che sia più sicura. |
    Examples:
      | newPassword    | passStrength |
      | pass           | Debole       |
      | mediumPass123! | Media        |

  Scenario: the client makes a POST request to the change-password endpoint with correct details and a new password that is the same as the old and receives expected body and response code
    Given Client provides following authentication credentials
      | username | userPassChange |
      | password | defaultPass    |
    When Client makes a "POST" request to "change-password" endpoint with the following body
      | username    | userPassChange |
      | password    | defaultPass    |
      | newPassword | defaultPass    |
    Then Client receives a status code of 500
    And The response has the following properties
      | description | User error                                           |
      | details     | La nuova password deve essere diversa dalla vecchia. |

  Scenario: the client makes a POST request to the change-password endpoint with correct details and receives expected body and response code, firstLogin gets updated on password changed
    When Client makes a "POST" request to "login" endpoint with the following body
      | username | userPassChange |
      | password | defaultPass    |
    And The response has the following properties
      | username   | userPassChange |
      | role       | user           |
      | attempts   | 0              |
      | firstLogin | true           |
    Given Client provides following authentication credentials
      | username | userPassChange |
      | password | defaultPass    |
    When Client makes a "POST" request to "change-password" endpoint with the following body
      | username    | userPassChange     |
      | password    | defaultPass        |
      | newPassword | StrongPassword23*! |
    Then Client receives a status code of 200
    And The response has the following text: "Password Cambiata."
    Given the headers are cleared
    When Client makes a "POST" request to "login" endpoint with the following body
      | username | userPassChange     |
      | password | StrongPassword23*! |
    Then The response has the following properties
      | username   | userPassChange |
      | role       | user           |
      | attempts   | 0              |
      | firstLogin | false          |

  Scenario: the client makes a POST request to the change-password endpoint with correct details with a body containing another user and receives expected body and response code
    Given Client provides "valid" "admin" authentication credentials
    When Client makes a "POST" request to "change-password" endpoint with the following body
      | username    | user        |
      | password    | defaultPass |
      | newPassword | newPass     |
    Then Client receives a status code of 403
    And The response has the following properties
      | description | User error                                       |
      | details     | You cannot change the password for another user. |

