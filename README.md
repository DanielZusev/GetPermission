[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![](https://jitpack.io/v/DanielZusev/GetPermission.svg)](https://jitpack.io/#DanielZusev/GetPermission)

# GetPermission
A library for simple access to runtime permission


## Setup
#### Add it in your root build.gradle at the end of repositories:
```gradle
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
#### Add the dependency
```gradle
dependencies {
	        implementation 'com.github.DanielZusev:GetPermission:Tag'
	}
```

## Usage
### First get an instance:
```java
getPermission = GetPermission.Builder()
                .with(this)
                .requestCode(REQUEST_MULTIPLE_PERMISSION)
                .setPermissionResultCallback(this)
                .askForPermission(PermissionTypes.CALENDAR, PermissionTypes.CAMERA, PermissionTypes.FINE_LOCATION)
                .message("These Permissions are required to work with all functions.")
                .build();
```

### Inorder to ask for permission call:

 ```java
getPermission.requestPermissions();
 ```






## License

    Copyright 2021 Daniel Zusev

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
