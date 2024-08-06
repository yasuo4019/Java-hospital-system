// vue.config.js
const fs = require('fs');
module.exports = {
    devServer: {
        https: {
            key: fs.readFileSync('C:/zhengshu/server-key.pem'),
            cert: fs.readFileSync('C:/zhengshu/server-cert.pem'),
            ca: fs.readFileSync('C:/zhengshu/ca.pem'),
        },
    }
};
