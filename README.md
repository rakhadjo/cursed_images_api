# Cursed Images API

An API designed initially for my Telegram Bot that returns random cursed images found in the internet. This program is currently **locally hosted**. I hope one day I will get it publicly hosted. In-depth documentation will come soon. 

Upon successful calling of the API, a response body will look like this:
```
{
	"rc": "00",
	"message": "Success",
	"imej": {
		"ObjectID": "....",
		"url": ...,
		"num": 3
		}
}
```
`rc` (response code) of `00` means the call is successful. 

Failed calls will look something like:
```
{
	"rc": "99",
	"message": "Undefined Error"
}
```