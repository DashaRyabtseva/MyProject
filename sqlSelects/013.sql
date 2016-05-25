select user_id, count(user_id) as COL from chat.messages where year(date) =  year(curdate()) and dayofyear(date) =  dayofyear(curdate()) group by user_id;


OR

select chat.users.name, count(chat.messages.user_id) as COL from (chat.messages left join chat.users on chat.users.id = chat.messages.user_id) where year(date) =  year(curdate()) and dayofyear(date) =  dayofyear(curdate()) group by name;