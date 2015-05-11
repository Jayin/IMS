## IMS-Server

当时由于技术原因，后台采用Java Socket + xml (模拟简单的http).

## `data/`


### MeetingData.xml

里面包含会议内容数据，参考:

```
<title>your title</title>
<meetingData>you content</meetingData>
```

### problem.txt

里面包含问题列表,一行一条问题，格式如下

```
userName@@@content
```

