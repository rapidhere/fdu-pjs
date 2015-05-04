/**
 * Author: rapidhere@gmail.com
 * FDU Web Project 1
 *
 * Personal page script
 */

$(document).ready(function() {
  'use strict';

  // tabs
  var $tabs = $('div.tab-nav');

  var _showTabs = function() {
    _.forEach($tabs, function($tab) {
      $tab = $($tab);
      var tabid = $tab.attr('data-navid');

      if($tab.attr('data-active') === 'active') {
        $('#' + tabid).show();
      } else {
        $('#' + tabid).hide();
      }
    });
  };

  _showTabs();

  $tabs.click(function() {
    var $tab = $(this);
    $tabs.removeAttr('data-active');
    $tab.attr('data-active', 'active');

    _showTabs();
  });
});
