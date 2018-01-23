module.exports = {
    open: function (type, success, failure) {
        if(type == null || type == '') type = '*/*';
        cordova.exec(success, failure, "CustomFileChooser", "open", [type]);
    },
    create: function (type, name, success, failure) {
        if(type == null || type == '') type = '*/*';
        cordova.exec(success, failure, "CustomFileChooser", "create", [type, name]);
    },
    getDatabasePath: function (name, success, failure) {
        cordova.exec(success, failure, "CustomFileChooser", "getDatabasePath", [name]);
    }
};
