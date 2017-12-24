package com.pos.leaders.leaderspossystem.CreditCard;

public class CreditCardResultException extends Exception {
    private String message;
    public CreditCardResultException(String msg) {
        this.message = msg;
    }

    @Override
    public String getMessage(){
        String str = this.message;
        switch (str){
            case "001":
                message = "כרטיס חסום";
                break;
            case "002":
                message = "כרטיס גנוב";
                break;
            case "003":
                message = "התקשר לחברת האשראי";
                break;
            case "004":
                message = "סירוב";
                break;
            case "005":
                message = "כרטיס מזויף";
                break;
            case "006":
                message = "שגויים CVV ת.ז.או";
                break;
            case "009":
                message = "לא הצליח להתקשר,התקשר לחברת האשראי";
                break;
            case "027":
                message = "כאשר לא הוכנס פס מגנטי כולו הגדר עסקה כעסקה טלפונית או כעסקת חתימה בלבד";
                break;
            case "033":
                message = "ככרטיס לא תקין";
                break;
            case "034":
                message = "כרטיס לא רשאי לבצע במסוף זה או אין אישור לעסקה כזאת";
                break;
            case "035":
                message = "כרטיס לא רשאי לבצע עסקה עם סוג אשראי זה";
                break;
            case "036":
                message = "פג תוקף";
                break;
            case "037":
                message = "כשגיאה בתשלומים - סכום עסקה צריך להיות שווה תשלום ראשון + (תשלום קבוע כפול מס' תשלומים)";
                break;
            case "038":
                message = "לא ניתן לבצע עסקה מעל תקרה לכרטיס לאשראי חיוב מיידי";
                break;
            case "039":
                message = "סיפרת בקורת לא תקינה";
                break;
            case "057":
                message = "לא הוקלד מספר תעודת זהות";
                break;
            case "058":
                message = "CVV2 לא הוקלד";
                break;
            case "059":
                message = "CVV2 - לא הוקלדו מספר תעודת הזהות וה";
                break;
            case "062":
                message = "כסוג עסקה לא תקין";
                break;
            case "063":
                message = "קוד עסקה לא תקין";
                break;
            case "064":
                message = "סוג אשראי לא תקין";
                break;
            case "065":
                message = "מטבע לא תקין";
                break;
            case "067":
                message = "קיים מספר תשלומים לסוג אשראי שאינו דורש זה";
                break;
            case "070":
                message = "לא מוגדר מכשיר להקשת מספר סודי";
                break;
            case "071":
                message = "כחובה להקליד מספר סודי";
                break;
            case "074":
                message = "דחייה -כרטיס נעול";
                break;
            case "075":
                message = "דחייה -פעולה עם קכ''ח לא הסתיימה בזמן הראוי";
                break;
            case "076":
                message = "דחייה -נתונים אשר התקבלו מקכ''ח אינם מוגדרים במערכת";
                break;
            case "077":
                message = "הוקש מספר סודי שגוי";
                break;
            case "101":
                message = "אין אישור מחברת אשראי לעבודה";
                break;
            case "107":
                message = "סכום העסקה גדול מידי - חלק למספר עסקאות";
                break;
            case "110":
                message = "למסוף אין אישור לכרטיס חיוב מיידי";
                break;
            case "111":
                message = "למסוף אין אישור לעסקה בתשלומים";
                break;
            case "112":
                message = "למסוף אין אישור לעסקה טלפון/חתימה בלבד בתשלומים";
                break;
            case "113":
                message = "למסוף אין אישור לעסקה טלפונית";
                break;
            case "114":
                message = "למסוף אין אישור לעסקה \"חתימה בלבד\"";
                break;
            case "115":
                message = "למסוף אין אישור לעסקה בדולרים";
                break;
            case "116":
                message = "למסוף אין אישור לעסקת מועדון";
                break;
            case "117":
                message = "למסוף אין אישור לעסקת כוכבים/נקודות/מיילים";
                break;
            case "118":
                message = "למסוף אין אישור לאשראי ישראקרדיט";
                break;
            case "119":
                message = "למסוף אין אישור לאשראי אמקס קרדיט";
                break;
            case "120":
                message = "למסוף אין אישור להצמדה לדולר";
                break;
            case "121":
                message = "למסוף אין אישור להצמדה למדד";
                break;
            case "124":
                message = "למסוף אין אישור לאשראי קרדיט בתשלומים לכרטיסי ישראכרט";
                break;
            case "125":
                message = "למסוף איו אישור לאשראי קרדיט בתשלומים לכרטיסי אמקס";
                break;
            case "133":
                message = "כרטיס לא תקף על פי רשימת כרטיסים תקפים של ישראכרט";
                break;
            case "134":
                message = "של ישראכרט) - מס' הספרות בכרטיס שגוי VECTOR1)  כרטיס לא תקין עפ\"י הגדרת המערכת";
                break;
            case "135":
                message = "של ישראכרט) VECTOR1) כרטיס לא רשאי לבצע עסקאות דולריות עפ\"י הגדרת המערכת";
                break;
            case "138":
                message = "ככרטיס לא רשאי לבצע עסקאות בתשלומים על פי רשימת כרטיסים תקפים של ישראכרט";
                break;
            case "140":
                message = "כרטיסי ויזה ודיינרס לא רשאים לבצע עסקאות מועדון בתשלומים";
                break;
            case "147":
                message = "כרטיס לא רשאי לבצע עסקאות בתשלומים עפ\"י וקטור 31 של לאומיקארד";
                break;
            case "148":
                message = "כרטיס לא רשאי לבצע עסקאות טלפוניות וחתימה בלבד עפ\"י ווקטור 31 של לאומיקארד";
                break;
            case "149":
                message = "כרטיס אינו רשאי לבצע עסקאות טלפוניות עפ\"י וקטור 31 של לאומיקארד";
                break;
            case "150":
                message = "אשראי לא מאושר לכרטיסי חיוב מיידי";
                break;
            case "151":
                message = "אשראי לא מאושר לכרטיסי חו\"ל";
                break;
            case "153":
                message = "של דיינרס) VECTOR21) . כרטיס לא רשאי לבצע עסקאות אשראי גמיש (עדיף 30+ /) עפ\"י הגדרת המערכת";
                break;
            case "154":
                message = "של דיינרס) VECTOR21) . כרטיס לא רשאי לבצע עסקאות חיוב מיידי עפ\"י הגדרת המערכת";
                break;
            case "155":
                message = "סכום המינימלי לתשלום בעסקת קרדיט קטן מידי";
                break;
            case "156":
                message = "מספר תשלומים לעסקת קרדיט לא תקין";
                break;
            case "157":
                message = "תקרה 0 לסוג כרטיס זה בעסקה עם אשראי רגיל או קרדיט";
                break;
            case "158":
                message = "תקרה 0 לסוג כרטיס זה בעסקה עם אשראי חיוב מיידי";
                break;
            case "159":
                message = "תקרה 0 לסוג כרטיס זה בעסקת חיוב מיידי בדולרים";
                break;
            case "160":
                message = "תקרה 0 לסוג כרטיס זה בעסקה טלפונית";
                break;
            case "161":
                message = "תקרה 0 לסוג כרטיס זה בעסקת זכות";
                break;
            case "162":
                message = "תקרה 0 לסוג כרטיס זה בעסקת תשלומים";
                break;
            case "173":
                message = "עסקה כפולה";
                break;
            case "200":
                message = "שגיאה יישומית";
                break;


            case "901":
                message = "שגיאה בהעברת נתונים";
                break;
            case "902":
                message = "לא נמצאה תוצאת עסקה, נסה שוב מאוחר יותר";
                break;
            case "903":
                message = "שגיאה כללית במשלוח שוברי עסקה";
                break;
            case "904":
                message = "שגיאה במשלוח שובר עסקה עותק בית העסק";
                break;
            case "905":
                message = "שגיאה במשלוח שובר עסקה עותק לקוח";
                break;
            case "906":
                message = "שגיאה במשלוח נתוני רישום";
                break;
            case "911":
                message = "שגיאת פיענוח או עיבוד נתונים";
                break;
            case "912":
                message = "מסוף לא פעיל";
                break;
            case "913":
                message = "מסוף אינו רשאי להפעיל אסימונים";
                break;
            case "914":
                message = "שגיאה לא מוגדרת בתפעול אסימונים";
                break;
            case "915":
                message = "המסוף ניצל את כל מיכסת האסימונים המוקצבת לו";
                break;
            case "916":
                message = "מספר השורות בקובץ גדול מ 9999 , נא לפצל לקבצים קטנים יותר";
                break;
            case "917":
                message = "בעיה בפיענוח קובץ חיובים מרובה שורות";
                break;
            case "918":
                message = "לא נמצא בסיס נתונים להעברה";
                break;
            case "919":
                message = "כישלון ביצירת בסיס נתונים מכווץ להעברה";
                break;
            case "920":
                message = "שגיאה בטיפול בחיווי גמר פעילות עם בסיס נתונים";
                break;
            case "921":
                message = "המסוף אינו רשאי לבצע פעולה זו";
                break;
            case "922":
                message = "אסימון או כרטיס לא קיים";
                break;
            case "923":
                message = "אסימון או כרטיס כבר קיים";
                break;
            case "998":
                message = "(WS) שגיאה פנימית בתהליך רישום סטטוס פעולה";
                break;
            case "999":
                message = "שרת או שירות אינו זמין";
                break;
            default:
                message = "שגיאה כללית";
                break;
        }
        return message + ". \n" + "מספר שגיאה: " + str;
    }
}
