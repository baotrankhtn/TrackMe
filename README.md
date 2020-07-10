# TrackMe
Tracking speed, distance and duration during workout sessions on Android devices

# Architecture and technologies: 
- MVVM architecture
- Libraries: Glide, Room, RxAndroid, Google location service

# Storage
<ul>
<li>Room session table contains: 
<ul>
<li>id: id of session, equals to the timestamp when session row is inserted</li>
<li>points: list of point, each point contains latitude, longitude, start_time (timestamp when previous point was added), end_time (timestamp when this point was added)</li>
<li>duration</li>
<li>distance</li>
</ul>
</ul>

- Route images are saved in internal storage (../images/), name of a route image is in [id_of_session].jpg format

# Record screen
<img src="https://user-images.githubusercontent.com/18632073/87135830-cbfdae00-c2c4-11ea-914c-6fdef12c8245.png" width="200"> <img src="https://user-images.githubusercontent.com/18632073/87135845-d15af880-c2c4-11ea-8215-c086b5003953.png" width="200">


- Start a forground service that contains FusedLocationProviderClient
- SupportMapFragment is used to show map and render markers, lines
- Animate GoogleMap camera to fit all coordinates
- Current speed is calculated on 3 latest coordinates
- When finishing a session, animate camera to fit all coordinates then take a snapshot of GoogleMap, save to internal storage. That image is the feature image of session in History screen. This application supposes that internet connection is available to render map information when ending an session 

# History screen
<img src="https://user-images.githubusercontent.com/18632073/87135841-d029cb80-c2c4-11ea-8b5b-34aa5e608339.png" width="200">

- Can load more when reaching the bottom of the list
- Feature image for each session is from internal storage
