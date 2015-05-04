/**
 * Author: rapidehere@gmail.com
 *
 * FDU Web Project 1
 *
 * Javascript for question page
 */

$(document).ready(function() {
  'use strict';

  var $buttons = $('span.reply-button');

  $buttons.click(function() {
    var $bt = $(this);

    var $container = $bt.parent().parent().find('div.reply-container');

    var notShowing = $container.attr('data-notshowing');
    notShowing = notShowing || 'showing';

    if(notShowing === 'notshowing') {
      $container.show();
      $container.attr('data-notshowing', 'showing');
    } else {
      $container.hide();
      $container.attr('data-notshowing', 'notshowing');
    }
  });

  $buttons.click();
});
