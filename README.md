# Cursed Images API

An API designed initially for my Telegram Bot that returns random cursed images found in the internet. This program is currently **locally hosted**. I hope one day I will get it publicly hosted. In-depth documentation will come soon.

## Usage
Headers are required at all times while making requests, apart from obtaining the key:
```
api_key = [String]
email   = [String]
```
### Obtaining an API Key
```
POST (host)/api/register
```
### Getting a Random Image
```
GET (host)/api/get
```
Successful response body:
```
{
	"rc": "00",
	"message": "Success",
	"imej": {
		"url": ...
		}
}
```
By default, `rc` (response code) of `00` means a successful call.

Failed response body:
```
{
	"rc": "99",
	"message": "Undefined Error"
}
```
### Uploading an Image
```
POST (host)/api/save
```
Required request body:
```
url = [String]
```
### Response Code Index:
...in progress


### Docker!
You can host the API without installing mongodb using docker.
There are two ways that the API can be run through docker:

The first is the easiest and doesn't require you to have maven but to spin it up it will take a long time.
Rename the "dockerfile-mvn" to "dockerfile"
Navigate to "cursed_images_api/cursed"
Run
```
docker-compose up --build
```

The second requires you have maven installed.
Navigate to "cursed_images_api/cursed"
Run
```
mvn clean package
```
This will compile the api.

Run
```
docker-compose up --build
```
This will build the docker image

Each time you make a change run these commands and your changes will be updated.
The first time you run them it will take longer as the images must be downloaded.
