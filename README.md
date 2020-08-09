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
