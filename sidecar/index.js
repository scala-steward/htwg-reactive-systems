const express = require('express');
const fs = require('fs');
const path = require('path');

const app = express();
const port = 3000;
var calls = 0;

app.get('/countries', (req, res) => {
  const filePath = path.join(__dirname, 'responses','countries', 'countries.json');

  fs.readFile(filePath, 'utf8', (err, data) => {
    if (err) {
      res.status(500).json({ error: 'Failed to read JSON file' });
    } else {
      try {
        const jsonData = JSON.parse(data);
        res.json(jsonData);
      } catch (parseError) {
        res.status(500).json({ error: 'Failed to parse JSON data' });
      }
    }
  });
});
app.get('/changes', (req, res) => {
    calls++;
    console.log("calls"+calls);
    // get params
    let change_type = req.query.change_type
    let services = req.query.services
    let cursor = req.query.cursor
    let country = req.query.country
    if (cursor == "cursor")
    {
      // this is for testing
        res.status(500).json({"message":"malformed cursor: cursor"});
        return;
    }
    if (country == "invalid_country_code")
    {
      // this is for testing
      console.log("invalid_country_code");
        res.status(500).json({"message":"This value invalid_code is not a valid country code, please provide a 2 letter ISO 3166-1 alpha-2 country code"});
        return;
    }
   // get random number between 1 and 4
    let random = Math.floor(Math.random() * 4) + 1;
    let filename = random+'.json';
    filePath = path.join(__dirname,'responses','changes',filename);
   
    fs.readFile(filePath, 'utf8', (err, data) => {
     if (err) {
       res.status(500).json({ error: 'Failed to read JSON file' });
     } else {
       try {
         const jsonData = JSON.parse(data);
         res.json(jsonData);
       } catch (parseError) {
         res.status(500).json({ error: 'Failed to parse JSON data' });
       }
     }
    }); 
  });

app.listen(port, () => {
  console.log(`Server is running on http://localhost:${port}`);
});
