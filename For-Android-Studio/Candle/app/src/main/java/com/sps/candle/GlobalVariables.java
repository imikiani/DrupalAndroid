package com.sps.candle;

public class GlobalVariables {
	
	public static String _Token = null;
	public static String _Cookie = null;
	
	public static String _Servername = "http://10.0.2.2/drupal/?q="; // Drupal installation path
	public static String _Service_endpoint = "newssrv/"; // Our endpoint that we create via services UI in drupal. Refer to [your-drupal-installation-path]/admin/structure/services
	public static String _RESOURCE_USER_LOGIN = "user/login/";
	public static String _RESOURCE_NODES_INDEX = "node/"; // we use HttpGet for indexing (listing) all nodes as well {server}/{endpoint}/node
	public static String _RESOURCE_NODE_CREATE = "node/";// we use HttpPOST for creating on node as well {server}/{endpoint}/node
	public static String _RESOURCE_FLAG_FLAG = "flag/flag/";
}
