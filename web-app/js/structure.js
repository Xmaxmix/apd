// INITIALIZING TREE
//$('#institution-tree').tree();
$('.icon-folder').click(function () {
  if ($(this).hasClass('icon-folder-open')){
    $(this).parent().siblings('.tree-folder-content').addClass('off');
    $(this).addClass('icon-folder-close');
    $(this).removeClass('icon-folder-open');
  }
  else if ($(this).hasClass('icon-folder-close')){
    $(this).parent().siblings('.tree-folder-content').removeClass('off');
    $(this).addClass('icon-folder-open');
    $(this).removeClass('icon-folder-close');
  }
});
