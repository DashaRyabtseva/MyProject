select user_id, count(user_id) as COL from chat.messages group by user_id having COL>3;
