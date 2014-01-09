var LDAP = (function(){
	var user;
	var group;
	var views = ['users', 'groups'];
	
	function initView(view){
		var time = 'fast';
		
		if($.inArray(view, views) != -1){
			$('.navbar-nav') 
				.find('li.active')
					.removeClass('active');

			$('a[href="#'+ view +'"]')
				.parent()
					.addClass('active');

			$('.container-view').slideUp(time);
			$('#' + view).slideDown(time);
		}
		else{
			$('a[href="#'+ defaultView +'"]')
			.parent()
				.addClass('active');

			$('#' + defaultView).slideDown(time);
			
		}
	}
	
	function initEvents(){

		$(window).bind('hashchange', function(e){ 
			initView(location.hash.substr(1));
		});
		
		if(errorMsg != ""){
			$('#info')
				.find('.text')
					.html(errorMsg)
					.end()
				.removeClass('hidden');
		}
		
		$('#add_user .modal-footer .btn,' + 
		  '#add_group .modal-footer .btn,' + 
		  '#add_group_user .modal-footer .btn,' + 
		  '#remove_group_user .modal-footer .btn,' + 
		  '#edit_group .modal-footer .btn,' + 
		  '#edit_user .modal-footer .btn').click(function(){
			$(this)
				.parents('.modal')
				.find('form')
					.submit();
		})
			
		$('a[data-target="#edit_user"]').click(function(){
			var $td = $(this).parents('td');
			var surname = $td.prev().html();
			var fullname = $td.prev().prev().html();
			var dn = $td.prev().prev().prev().html();
			user = {
				'dn' : dn,
				'fullname' : fullname,
				'surname' : surname
			};
		});
		
		$('a[data-target="#edit_group"], a[data-target="#add_group_user"], a[data-target="#remove_group_user"]').click(function(){
			var $td = $(this).parents('td');
			var groupname = $td.prev().prev().html();
			var dn = $td.prev().prev().prev().html();
			group = {
				'dn' : dn,
				'groupname' : groupname
			};
		});
		
		$('#edit_user').on('show.bs.modal', function (e) {
			$(this)
				.find('form')
					.find('input[name="dn"]')
						.val(user.dn)
					.end()
					.find('input[name="cn"]')
						.val(user.fullname)
					.end()
					.find('input[name="fullname"]')
						.val(user.fullname)
					.end()
					.find('input[name="surname"]')
						.val(user.surname)
					
		});
		
		$('#edit_group').on('show.bs.modal', function (e) {
			$(this)
				.find('form')
					.find('input[name="dn"]')
						.val(group.dn)
					.end()
					.find('input[name="cn"]')
						.val(group.groupname)
					.end()
					.find('input[name="groupname"]')
						.val(group.groupname)
					
		});
		
		$('#add_group_user').on('show.bs.modal', function (e) {
			$(this)
				.find('form')
					.find('input[name="groupname"]')
						.val(group.groupname)
					
		});
		
		$('#remove_group_user').on('show.bs.modal', function (e) {
			$(this)
				.find('form')
					.find('input[name="groupname"]')
						.val(group.groupname)
					
		});

	}
	
	function init(){
		initEvents();
		if(location.hash != '') initView(location.hash.substr(1));
		else initView();
	}
	
	return{
		init : init
	}
})();

$(function(){
	LDAP.init();
});