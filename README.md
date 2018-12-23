# WalmartApp
Sample app for Interview

1) This app shows list of scrollable movies along with details like Title, Released date, and rating.
2) Image loading is done using Glide - third party image loading and caching library.
3) App uses RecyclerView as opposed ListView to show the list of images, couple of reasons for which -
   RecyclerView mandates ViewHolder pattern, and
   offers flexible layout management (if there is a need to show items in a Grid Layout or any
   other layout in the future.).
4) The app has MainActivity to show the first list of items and DetailActivity to show item details
   in a new view.
5) Serialization and Deserialization of list item data is done thru JSONObject's toString() method as
   opposed to creating Parcelable class from Android. JSONObject's toString() offers simple way to
   send intentExtra as a String and new JSONObject creation from a String.
6) Currently the app shows a button for movie trailer. 
6) The app can be extended to show movie's trailer in a new fragment. To make this happen, a container
   needs to be created that can add/replace DetailFragment with TrailerFragment and vice versa.

