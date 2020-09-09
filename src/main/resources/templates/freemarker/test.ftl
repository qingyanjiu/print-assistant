<style type="text/css">
	div{
		font-family: 'Helvetica Neue', Helvetica, 'Microsoft Yahei', 'Hiragino Sans GB', 'WenQuanYi Micro Hei', sans-serif;
	}
	a{
	   	color: #337ab7;
	  	text-decoration: underline;
	}
	th{
  		text-align: left;
		border-bottom: 2px solid #ddd;
		padding: 8px;
	}
	td{
		border-top: 1px solid #ddd;
		padding: 8px;
	}
	.light-row {
		background-color: #f9f9f9;
	}
    .Healthy,.Warning,.Alert,.Exception,.Unknown
    {
    	color: #fff;
    }
    .Healthy{
    	background: #A5DE37
    }
    .Warning{
		background: #FFD700;
    }
    .Alert{
    	background: #FF7F00;
    }
    .Exception {
        background: #CD0000;
    }
    .Unknown {
        background: #696969;
    }
</style>
<div>
${name} <br/>
<div style="color:red">${text}</div>
</div>
