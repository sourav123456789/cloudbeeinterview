# Added the API URLS FOR BETTER NAVIGTION

# Import the curls to postman so that you can directly use the endpoints.

1 - To book a ticket . This is generate a book a random seat based on availablity.

  curl --location 'http://localhost:8080/ticket/' \
--header 'Content-Type: application/json' \
--data-raw '{
    "firstName" : "Sourav",
    "lastName" : "Panigrahi",
    "email" : "souravpanigrahi79@gmail.com"
}'

2 - To get the receipt of a user . It will give all the tickets booked by the user.


curl --location 'http://localhost:8080/receipt/souravpanigrahi78@gmail.com'

3 - To get All the booked seat details of a section .

curl --location 'http://localhost:8080/ticket/ticketBySection/A'

4 - To delete all the tickets booked by the user.

curl --location --request DELETE 'http://localhost:8080/ticket/souravpanigrahi79@gmail.com'

5 - To update the seat of a User . This Endpoint will delete all the tickets booked by the user and book new ticket for the user. User has to give the section and ticketNo in the requestBody

curl --location --request PUT 'http://localhost:8080/ticket/' \
--header 'Content-Type: application/json' \
--data-raw '{
    "user": {
        "firstName": "Sourav",
        "lastName": "Panigrahi",
        "email": "souravpanigrahi78@gmail.com"
    },
    "info" : {
        "section" : "A",
        "ticketNo" : "A123"
    }
}'


6 - To replace the booked ticket with a different Seat.


curl --location --request PUT 'http://localhost:8080/ticket/replaceTicket' \
--header 'Content-Type: application/json' \
--data-raw '{
    "user": {
        "firstName": "Sourav",
        "lastName": "Panigrahi",
        "email": "souravpanigrahi78@gmail.com"
    },
    "booked": {
        "ticketNo": "A279",
        "section": "A"
    },
    "replace": {
        "ticketNo": "A280",
        "section": "A"
    }
}'

