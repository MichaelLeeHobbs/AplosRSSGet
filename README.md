# AplosRSSGet
Aplos RSS Get reads a list of feeds from a DB and then pulls down the feeds and logs them back into the DB.

Simply put Aplos RSS Get is a process that you run using a KRON job once a day or more often as you choose. That will
query a DB for a listing of RSS url's. It will then download these RSS feeds and log them back into the DB.

Aplos RSS Get was created for and used by the AM PodCast Network http://www.ampodcastnetwork.com/
