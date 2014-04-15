lorol-rss
=================
Dashclock plugin based on Jsoup that displays parts of a RSS, XML or HTML webpage as several lines.
You can validate Jsoup selecting patterns at:
http://try.jsoup.org/ 
by using a PC or another device with web browser

- Select the "Fetch URL" and optionally the user agent string (Android ...)
- Populate the Jsoup pattern at "CSS Query"
- Choose XML or HTML parser in Settings if needed.
- Check the output and adjust the "CSS Query" interactively until you see the parts you want
- Choose the index sequence (max 4) you want to display on dachclock, example 0,1,2
- Populate all parameters in Dashclock plugin on your phone.


Note, as long as your phone has Internet data connection it will automatically visit the web page in question about every hour. 
If you "touch for refresh" often or during tests these visits will be more frequent and the server may reject your requests.
Do not abuse the correspondent web services.

The idea started from https://github.com/mridang/ 