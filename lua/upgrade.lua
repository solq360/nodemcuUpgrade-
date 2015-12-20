--[[
 lua socket upgrade
 by solq
--]]
last_str = nil;
upgrade_port = 8888;

function wifi_info()
 print(wifi.sta.getip())
    print(wifi.sta.getmac())
end 

function string_split(s, delim)
    if type(delim) ~= "string" or string.len(delim) <= 0 then
        return
    end

    local start = 1
    local t = {}
    while true do
  local pos = string.find (s, delim, start, true) -- plain find
        if not pos then
          break
        end

        table.insert (t, string.sub (s, start, pos - 1))
        start = pos + string.len (delim)
    end
    table.insert (t, string.sub (s, start))

    return t
end
 command = {
  FILE_UP = function(data)
   file.writeline(data);
   --do open not enough memory
   --print("writeline file :",data)
  end,
  FILE_CREATE = function(data)
   local name = data;
   file.remove(name);
   file.open(name,"w+");
   print("remove file :",data)
   print("open file :",data)
  end,
  FILE_CLOSE = function(data)
   file.close(data);
   print("close file :",data)
  end,
  FILE_REMOVE = function(data)
   file.remove(data);
   print("remove file :",data)
  end ,
  RUN = function(data)
   dofile(data);
   print("run file :",data)
  end,
  RE_BOOT = function(data)
   print("restart");
   node.restart();
  end,
 };
function upgrade_server_setup()
 
 srv=net.createServer(net.TCP,60) 
 srv:listen(upgrade_port,function(conn) 
  conn:on("connection",function(client) 
   last_str=nil; 
   print("connection client");
  end);
  conn:on("disconnection",function(client) 
   collectgarbage(); 
   print("disconnection client");  
  end);
  conn:on("receive",function(client,payload) 
   local str = payload;
   payload = nil;
   if last_str ~= nil then
    str = last_str .. str;
    last_str = nil;
   end
   local start = 0;
   local delim = "\n";
   local delimLen = string.len(delim);
   local text = nil;
   while true do
      local pos = string.find (str,delim,start);    
      if not pos then
     last_str =  string.sub (str, start);
      break
    end
    text  = string.sub (str, start, pos - 1);
    --------------------
    local ar = string_split(text,":abcde123834:");
    if command[ar[1]] == nil then
     print("protocol error ",text);
     text = nil;
     return;
    end
    text = nil;
    command[ar[1]](ar[2]);
    ar = nil;
    collectgarbage();
     --tmr.delay(1000);
    start = pos + delimLen
   end
   end) 
 end)
 wifi_info();
 print("start upgrade server : "..upgrade_port);
end

upgrade_server_setup();