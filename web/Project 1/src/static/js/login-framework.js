/**
 * Author: rapidhere@gmail.com
 *
 * FDU Web Project 1
 *
 * For header bar
 */

$(document).ready(function() {
  'use strict';

  var $search = $('div.search-container input');
  var $i = $('div.search-container i');
  var $button = $('div.search-container button');

  $search.on('keypress', function(e) {
    if(e.keyCode === 13) {
      $button.click();
      e.preventDefault();
    }
  });

  $search.on('input', function() {
    var length = $search.val().length;

    if(! length) {
      $i.attr('class', 'fa fa-search');
    } else {
      $i.attr('class', 'fa fa-share');
    }
  });
});
