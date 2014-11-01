var serverUrlRoot = '/BackboneExample/api/users';

$.fn.serializeObject = function () {
    var o = {};
    var a = this.serializeArray();
    $.each(a, function () {
        if (o[this.name] !== undefined) {
            if (!o[this.name].push) {
                o[this.name] = [o[this.name]];
            }
            o[this.name].push(this.value || '');
        } else {
            o[this.name] = this.value || '';
        }
    });
    return o;
};

var Users = Backbone.Collection.extend({
    url: serverUrlRoot + '/all'
});

var User = Backbone.Model.extend({
        urlRoot: serverUrlRoot
    }
);

var UserList = Backbone.View.extend({
    el: '.page',
    render: function () {
        var that = this;
        var users = new Users();
        users.fetch({
            success: function (users) {
                var template = _.template($('#user-list-template').html(), {users: users.models});
                that.$el.html(template)
            }
        })
    }
});

var EditUser = Backbone.View.extend({
    el: '.page',
    render: function (options) {
        var that = this;
        if (options.id) {
            console.log(options.id);
            var user = new User({id: options.id});
            user.fetch({
                success: function (user) {
                    var template = _.template($('#edit-user-template').html(), {user: user});
                    that.$el.html(template)
                }
            })
        } else {
            var template = _.template($('#edit-user-template').html(), {user: null});
            this.$el.html(template)
        }
    },
    events: {
        'submit .edit-user-form': 'saveUser'

    },
    saveUser: function (ev) {
        var userDetails = $(ev.currentTarget).serializeObject();
        var user = new User();
        user.save(userDetails, {
            success: function (user) {
                router.navigate('', {trigger: true});
            }
        });
        console.log(userDetails);
        return false;
    }
});

var DeleteUser = Backbone.View.extend({
    render: function (options) {
        console.log(options.id);
        var user = new User({id: options.id});
        user.fetch({
            success: function (user) {
                user.destroy({
                    success: function () {
                        router.navigate('', {trigger: true});
                    }
                })
            }
        })
    }
})

var Router = Backbone.Router.extend({
    routes: {
        '': 'home',
        '/new': 'editUser',
        '/edit/:id': 'editUser',
        '/delete/:id': 'deleteUser'
    }
});

var userList = new UserList();
var editUser = new EditUser();
var deleteUser = new DeleteUser();

var router = new Router();

router.on('route:home', function () {
    userList.render();
});

router.on('route:editUser', function (id) {
    editUser.render({id: id});
});

router.on('route:deleteUser', function (id) {
    deleteUser.render({id: id});
});

Backbone.history.start();

