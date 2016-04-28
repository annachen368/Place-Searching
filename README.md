# Place Searching

<img src="http://i.imgur.com/3Uq0joD.png" width="288" height="480" />
<img src="http://i.imgur.com/amrni57.png" width="288" height="480" />
<img src="http://i.imgur.com/Bky2fzS.png" width="288" height="480" />

#### 1. Error conditions I encountered: How should these be handled?

1.FileNotFoundException

>  java.io.FileNotFoundException: https://maps.googleapis.com/maps/api/place/autocomplete/json?input=mountain &types=geocode&language=en&key=AIzaSyC5uHROOrPPPbyCN40nIEx90laVue8pjYY

Solution: I replaced all spaces of input with '+'.

2.Exception dispatching input event

> 04-27 20:09:25.690 2789-2789/com.example.anna.placesearching E/InputEventReceiver: Exception dispatching input event.
04-27 20:09:25.691 2789-2789/com.example.anna.placesearching E/MessageQueue-JNI: Exception in MessageQueue callback: handleReceiveCallback
04-27 20:09:25.692 2789-2789/com.example.anna.placesearching E/MessageQueue-JNI: java.lang.NullPointerException: Attempt to invoke virtual method 'android.database.Cursor android.support.v4.widget.CursorAdapter.getCursor()' on a null object reference

Solution: I implemented "searchView.setOnSuggestionListener" 

#### 2. Where could the user experience break? How will you prevent this?

If there is no Wifi or a Signal, users cannot search for the places. Check if the JSON equals to null before parsing it.

#### 3. What other improvements can be made?

+ Cache the input: if the input has already been stored at HashTable or Pool, there is no need to download the places information again.
+ Display map: we have lat and lng so it will be great to display location on Google map.
+ Custom adapter: it allows us to add place_id and description(place name) while parsing the JSON at the same time.

