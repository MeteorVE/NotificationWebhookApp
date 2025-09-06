# Enhanced Webhook Payload Documentation

## Previous Webhook Format
The original implementation only sent the notification text:
```
"Hello, this is a notification message"
```

## New Enhanced Webhook Format
The enhanced implementation now sends a complete JSON object with all available notification data:

```json
{
  "package": "com.whatsapp",
  "postTime": 1638360000000,
  "id": 12345,
  "tag": "message_tag",
  "key": "0|com.whatsapp|12345|null|10150",
  "title": "John Doe",
  "text": "Hello, how are you?",
  "subText": "WhatsApp",
  "bigText": "Hello, how are you? This is a longer message that shows in the expanded notification view.",
  "summaryText": "2 new messages",
  "infoText": "Additional info",
  "conversationTitle": "John Doe",
  "channelId": "messages",
  "category": "msg",
  "priority": 1,
  "visibility": 1,
  "flags": 16,
  "group": "message_group",
  "sortKey": "0000",
  "actions": ["Reply", "Mark as read"]
}
```

## Field Descriptions

### Basic Notification Information
- **package**: Source application package name (e.g., "com.whatsapp")
- **postTime**: Timestamp when notification was posted (Unix timestamp in milliseconds)
- **id**: Unique notification ID within the package
- **tag**: Optional tag associated with the notification
- **key**: Unique key identifying this notification

### Content Fields
- **title**: Main notification title
- **text**: Primary notification text content
- **subText**: Secondary text (often app name or category)
- **bigText**: Expanded text content when notification is expanded
- **summaryText**: Summary text for grouped notifications
- **infoText**: Additional informational text
- **conversationTitle**: Title for conversation-style notifications
- **channelId**: Notification channel identifier

### Metadata Fields
- **category**: Notification category (e.g., "msg", "call", "email")
- **priority**: Notification priority level (-2 to 2)
- **visibility**: Visibility on lock screen (0=secret, 1=private, 2=public)
- **flags**: Notification flags as bitmask
- **group**: Group key for notification grouping
- **sortKey**: Key for sorting notifications within a group

### Actions
- **actions**: Array of action button titles available on the notification

## Benefits of Enhanced Format

1. **Complete Context**: Provides full notification context instead of just text
2. **Better Analytics**: Enables detailed notification analysis and filtering
3. **App Identification**: Clear source app identification via package name
4. **Timing Information**: Precise timestamp for notification events
5. **Rich Content**: Access to all notification extras and metadata
6. **Action Tracking**: Visibility into available notification actions

## Backward Compatibility

The enhanced implementation maintains backward compatibility:
- If new notification data extraction fails, falls back to sending just the text field
- Existing webhook endpoints will continue to receive data (though in enhanced JSON format)
- All null/empty fields are properly handled and omitted from JSON when appropriate