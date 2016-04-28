# Place Searching

<img src="http://i.imgur.com/3Uq0joD.png" width="288" height="480" />
<img src="http://i.imgur.com/amrni57.png" width="288" height="480" />
<img src="http://i.imgur.com/Bky2fzS.png" width="288" height="480" />

#### Error conditions I encountered: How should these be handled?

1.FileNotFoundException

>  java.io.FileNotFoundException: https://maps.googleapis.com/maps/api/place/autocomplete/json?input=mountain &types=geocode&language=en&key=AIzaSyC5uHROOrPPPbyCN40nIEx90laVue8pjYY

Solution: I replaced all space of input with '+'.

2.Exception dispatching input event

> 04-27 20:09:25.690 2789-2789/com.example.anna.placesearching E/InputEventReceiver: Exception dispatching input event.
04-27 20:09:25.691 2789-2789/com.example.anna.placesearching E/MessageQueue-JNI: Exception in MessageQueue callback: handleReceiveCallback
04-27 20:09:25.692 2789-2789/com.example.anna.placesearching E/MessageQueue-JNI: java.lang.NullPointerException: Attempt to invoke virtual method 'android.database.Cursor android.support.v4.widget.CursorAdapter.getCursor()' on a null object reference

Solution: I implemented "searchView.setOnSuggestionListener" 

#### Where could the user experience break? How will you prevent this?

#### What other improvements can be made?

