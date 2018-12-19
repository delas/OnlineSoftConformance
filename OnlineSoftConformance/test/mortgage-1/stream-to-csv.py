import xmltodict

filepath = 'log.stream'
with open(filepath) as fp:  
	line = fp.readline()
	while line:
		log = xmltodict.parse(line)
		trace = log["org.deckfour.xes.model.impl.XTraceImpl"]["log"]["trace"]["string"]
		event = log["org.deckfour.xes.model.impl.XTraceImpl"]["log"]["trace"]["event"]["string"]
		print trace["@value"] + "," + event["@value"]
		line = fp.readline()
