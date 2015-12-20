l = file.list();
for k,v in pairs(l) do
	file.remove(k);
	print("remove:",k);
end