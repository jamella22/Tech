const Sequalize = require('sequelize');

module.exports = function (sequalize) {
    const Article = sequalize.define('Article', {

        title:{
            type: Sequalize.STRING,
            required: true,
            allowNull: false,
        },
        content:{
            type: Sequalize.TEXT,
            required: true,
            allowNull: false,
        },
        image: {
            type: Sequalize.TEXT,
            allowNull: false,
            required: false
        },
    });

    Article.associate = function (models) {
        Article.belongsTo(models.User, {
            foreignKey: 'authorId',
            targetKey: 'id'
        });
    };
    return Article;
};