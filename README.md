# WeatherInfo
Weather API Public key : You need to replace api key with your own key here-> Go to utils foler->open constants class -> const val API_KEY  = "key"
Technoloy used
Hilt dependency injection: to create single instance of the important classes that is reusing in the application
Room database: to store user information
Data binding : to bind ui with class 
view model  : to hold lagre amount of data and prevent data lose issue during orientation change
Live data   : to observ data from the view model
Retrofit2   : to calling API and fetch data from the remote database
FusedLocationProvider : to get current location(lattitude, longitude) with help of google location service.
Geocoder   : to get current country name and city name with the help of lattitude and longitude.
Glide      : to upload image on the view.
View Pager : to creating tab layout with fragment
