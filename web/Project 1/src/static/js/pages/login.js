/**
 * Author: rapidhere@gmail.com
 *
 * runtime js for login page
 */

$(document).ready(function() {
  'use strict';

  // current displayed
  var cur = 'login';

  var $login = $('#login-container');
  var $register = $('#register-container');
  var $click = $('#login-register-nav a');

  // toggle login/register
  $click.click(function() {
    if(cur === 'login') {
      $register.hide();
      $login.show();
      $click.html('注册&nbsp;<i class="fa fa-caret-right"></i>');
      cur = 'register';
    } else {
      $login.hide();
      $register.show();
      $click.html('登录&nbsp;<i class="fa fa-caret-right"></i>');
      cur = 'login';
    }
  });

  var _resizePortrait = function() {
    // sort the portraits
    var $portraitContainer = $('div.portrait-container');
    var $portraits = $portraitContainer.find('div.portrait-section');
    var $descriptions = $portraitContainer.find('div.description-section');
    var $names = $descriptions.find('p.name');

    // hide descriptions
    $descriptions.hide();

    // get the width
    var width = $portraitContainer.width();
    var imgWidth = 105;

    var margin = parseInt((width - imgWidth * 4) / 3) ;

    if(margin >= 160) {
      $descriptions.show();
    }

    // recacluate the margin
    margin = parseInt((width - $portraits.width() * 4) / 3) - 5;

    $portraits.css({
      'margin-right': '' + margin + 'px'
    });

    $($portraits[3]).css({'margin-right': 0});
    $($portraits[7]).css({'margin-right': 0});
  };

  _resizePortrait();
  $(window).on('resize', _resizePortrait);

  // filter
  var passwordFilter = function(password) {
    if(password.length < 6)
      return '密码长度过短';
    if(password.length > 16)
      return '密码过长';

    if(! password.match(/^[a-zA-Z0-9]+$/))
      return '密码只能包含英文和数字';

    if(password.match(/^[0-9]+$/))
      return '密码不能是纯数字';

    return null;
  };

  var usernameFilter = function(username) {
    if(username.length < 2)
      return '用户名过短';

    if(username.length > 16)
      return '用户名过长';

    return null;
  };

  var emailPattern = /^([\w-]+(?:\.[\w-]+)*)@((?:[\w-]+\.)*\w[\w-]{0,66})\.([a-z]{2,6}(?:\.[a-z]{2})?)$/i;
  var emailFilter = function(email) {
    if(! email.match(emailPattern))
      return '邮箱格式不正确';

    return null;
  };

  // handle login
  var $loginErrorInfo = $('#login-error-info');

  $('#login-container button').click(function() {
    // check email
    var val = $('#login-container input[name="email"]').val();
    var err = emailFilter(val);

    if(err) {
      $loginErrorInfo.text(err);
      $loginErrorInfo.show();
      return ;
    }

    // check password
    val = $('#login-container input[name="password"]').val();
    err = passwordFilter(val);

    if(err) {
      $loginErrorInfo.text(err);
      $loginErrorInfo.show();
      return ;
    }

    // TODO: submit form
    // refresh form in this version
    $('#login-container form')[0].reset();
    $loginErrorInfo.hide();

    window.location.href = './question.html';
  });

  // handle register
  var $registerErrorInfo = $('#register-error-info');

  $('#register-container button').click(function() {
    // check username
    var val = $('#register-container input[name="username"]').val();
    var err = usernameFilter(val);

    if(err) {
      $registerErrorInfo.text(err);
      $registerErrorInfo.show();
      return ;
    }

    // check email
    val = $('#register-container input[name="email"]').val();
    err = emailFilter(val);

    if(err) {
      $registerErrorInfo.text(err);
      $registerErrorInfo.show();
      return ;
    }

    // check password
    val = $('#register-container input[name="password"]').val();
    err = passwordFilter(val);

    if(err) {
      $registerErrorInfo.text(err);
      $registerErrorInfo.show();
      return ;
    }

    // TODO: submit form
    // refresh form in this version
    $('#register-container form')[0].reset();
    $registerErrorInfo.hide();
  });

});
