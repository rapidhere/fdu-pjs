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
});
