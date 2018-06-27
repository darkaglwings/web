function load(header, data, setting, isUseCookie) {
	gridTree = new Core4j.toolbox.TableTree4j({
		columns:setting,
		treeMode:'grid',
		renderTo:'tabletree',
		useLine:true,
		useIcon:true,
		id:'grid',
		useCookie:false,
		headers:header,
		themeName:'arrow',
		selectMode:'single',
		useCookie:isUseCookie,
		cookieTime:0,
		
		onCollapseNodeEvents:[onExpandNode],
		onExpandNodeEvents:[onExpandNode]
	});

	gridTree.build(data, true);
	
	if (isUseCookie) {
		var nodes = gridTree.getCookieExpandNodes(true);
		for (var i = 0 ; i < nodes.length; i++) {
			gridTree.toggleNode(nodes[i], true);
		}
	}
}

function onExpandNode(node, treeTable) {
	treeTable.saveNodeCooikeByToggleNode(node);
}

