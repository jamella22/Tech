const Sequelize = require('sequelize');

module.exports = function (sequelize) {
    let Cat = sequelize.define("Cat",{
        //TODO

    },{
        timestamps:false
    });

    return Cat;
};