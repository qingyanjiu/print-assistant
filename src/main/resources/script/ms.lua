--获取KEY
local key = KEYS[1]
----返回的变量
local result = {}
----打印日志到reids
----注意，这里的打印日志级别，需要和redis.conf配置文件中的日志设置级别一致才行
redis.log(redis.LOG_DEBUG,key)
----往redis设置值
local nu = redis.call("get",key)
if (tonumber(nu) > 0) then
  redis.call("decr",key)
  return true;
else
  return false;
end