dojo.require("dojo.fx");
dojo.addOnLoad(function() {

});

var img = '';

function showPicture(file) {
	if (img!='') dojo.byId('hidden-pictures').appendChild(img);
	img = dojo.byId('img-'+file);
	picture = dojo.byId('picture');
	picture.appendChild(img);
	board = dojo.byId('board');

	if (picture.style.display != 'block') {		
		picture.style.display = 'block';
		
		pictureAnim = dojo.fadeIn({
			node: 'picture'
		});
		boardAnim = dojo.fadeOut({
			node: 'board',
			onEnd: function() { board.style.display='none'; }
		});
		dojo.fx.combine([pictureAnim, boardAnim]).play();	
	}
}

function showBoard() {
	picture = dojo.byId('picture');
	board = dojo.byId('board');
	board.style.display = 'block';

	dojo.byId('hidden-pictures').appendChild(img);
	
	pictureAnim = dojo.fadeOut({
		node: 'picture',
		onEnd: function() { picture.style.display='none'; }
	});
	boardAnim = dojo.fadeIn({
		node: 'board'
	});
	dojo.fx.combine([pictureAnim, boardAnim]).play();
}