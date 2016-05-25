select user_id, count(user_id) as COL from chat.messages where date BETWEEN "2016-05-09 00:00:00" and "2016-05-09 23:59:59" group by user_id;

OR

select chat.users.name, count(chat.messages.user_id) as COL from (chat.messages left join chat.users on chat.users.id = chat.messages.user_id) where date BETWEEN "2016-11-12 00:00:00" and "2016-11-12 23:59:59" group by name;