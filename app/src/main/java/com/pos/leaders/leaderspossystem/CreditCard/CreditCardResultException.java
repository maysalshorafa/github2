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
            case "000":
                message = "מאושר";
                break;
            case "001":
                message = "כרטיס חסום";
                break;
            case "002":
                message = "גנוב החרם כרטיס";
                break;
            case "003":
                message = "התקשר לחברת האשראי";
                break;
            case "004":
                message = "העסקה לא אושרה";
                break;
            case "005":
                message = "כרטיס מזוייף החרם";
                break;
            case "006":
                message = "דחה עסקה: cvv2 שגוי";
                break;
            case "007":
                message = "דחה עסקה: cavv/ucaf שגוי";
                break;
            case "008":
                message = "דחה עסקה: avs שגוי";
                break;
            case "009":
                message = "דחייה - נתק בתקשורת";
                break;
            case "010":
                message = "אישור חלקי";
                break;
            case "011":
                message = "דחה עסקה: חוסר בנקודות/כוכבים/מיילים/הטבה אחרת";
                break;
            case "012":
                message = "בכרטיס לא מורשה במסוף";
                break;
            case "013":
                message = "דחה בקשה .קוד יתרה שגוי";
                break;
            case "014":
                message = "דחייה .כרטיס לא משוייך לרשת";
                break;
            case "015":
                message = "דחה עסקה: הכרטיס אינו בתוקף";
                break;
            case "016":
                message = "דחייה -אין הרשאה לסוג מטבע";
                break;
            case "017":
                message = "דחייה -אין הרשאה לסוג אשראי בעסקה";
                break;
            case "026":
                message = "דחה עסקה - idשגוי";
                break;
            case "041":
                message = "ישנה חובת יציאה לשאילתא בגין תקרה בלבד לעסקה עם פרמטר j2";
                break;
            case "042":
                message = "ישנה חובת יציאה לשאילתא לא רק בגין תקרה, לעסקה עם פרמטר j2";
                break;
            case "051":
                message = "חסר קובץ ווקטור 1";
                break;
            case "052":
                message = "חסר קובץ ווקטור 4";
                break;
            case "053":
                message = "חסר קובץ ווקטור 6";
                break;
            case "055":
                message = "חסר קובץ ווקטור 11";
                break;
            case "056":
                message = "חסר קובץ ווקטור 12";
                break;
            case "057":
                message = "חסר קובץ ווקטור 15";
                break;
            case "058":
                message = "חסר קובץ ווקטור 18";
                break;
            case "059":
                message = "חסר קובץ ווקטור 31";
                break;
            case "060":
                message = "חסר קובץ ווקטור 34";
                break;
            case "061":
                message = "חסר קובץ ווקטור 41";
                break;
            case "062":
                message = "חסר קובץ ווקטור 44";
                break;
            case "063":
                message = "חסר קובץ ווקטור 64";
                break;
            case "064":
                message = "חסר קובץ ווקטור 80";
                break;
            case "065":
                message = "חסר קובץ ווקטור 81";
                break;
            case "066":
                message = "חסר קובץ ווקטור 82";
                break;
            case "067":
                message = "חסר קובץ ווקטור 83";
                break;
            case "068":
                message = "חסר קובץ ווקטור 90";
                break;
            case "069":
                message = "חסר קובץ ווקטור 91";
                break;
            case "070":
                message = "חסר קובץ ווקטור 92";
                break;
            case "071":
                message = "חסר קובץ ווקטור 93";
                break;
            case "073":
                message = "חסר קובץ PARAM_3_1";
                break;
            case "074":
                message = "חסר קובץ PARAM_3_2";
                break;
            case "075":
                message = "חסר קובץ PARAM_3_3";
                break;
            case "076":
                message = "חסר קובץ PARAM_3_4";
                break;
            case "077":
                message = "חסר קובץ PARAM_361";
                break;
            case "078":
                message = "חסר קובץ PARAM_363";
                break;
            case "079":
                message = "חסר קובץ PARAM_364";
                break;
            case "080":
                message = "חסר קובץ PARAM_61";
                break;
            case "081":
                message = "חסר קובץ PARAM_62";
                break;
            case "082":
                message = "חסר קובץ PARAM_63";
                break;
            case "083":
                message = "כרטיס לא רשאי לבצע עסקאות טלפוניות וחתימה בלבד עפ\"י ווקטור 31 של לאומיקארד";
                break;
            case "084":
                message = "חסר קובץ CEIL_42";
                break;
            case "085":
                message = "חסר קובץ CEIL_43";
                break;
            case "086":
                message = "חסר קובץ CEIL_44";
                break;
            case "087":
                message = "חסר קובץ DATA";
                break;
            case "088":
                message = "חסר קובץ JENR";
                break;
            case "089":
                message = "חסר קובץ Start";
                break;
            case "101":
                message = "חסרה כניסה בוקטור 1";
                break;
            case "103":
                message = "חסרה כניסה בוקטור 4";
                break;
            case "104":
                message = "חסרה כניסה בוקטור 6";
                break;
            case "106":
                message = "חסרה כניסה בוקטור 11";
                break;
            case "107":
                message = "חסרה כניסה בוקטור 12";
                break;
            case "108":
                message = "חסרה כניסה בוקטור 15";
                break;
            case "110":
                message = "חסרה כניסה בוקטור 18";
                break;
            case "111":
                message = "חסרה כניסה בוקטור 31";
                break;
            case "112":
                message = "חסרה כניסה בוקטור 34";
                break;
            case "113":
                message = "חסרה כניסה בוקטור 41";
                break;
            case "114":
                message = "חסרה כניסה בוקטור 44";
                break;
            case "116":
                message = "חסרה כניסה בוקטור 64";
                break;
            case "117":
                message = "חסרה כניסה בוקטור 81";
                break;
            case "118":
                message = "חסרה כניסה בוקטור 82";
                break;
            case "119":
                message = "חסרה כניסה בוקטור 83";
                break;
            case "120":
                message = "חסרה כניסה בוקטור 90";
                break;
            case "121":
                message = "חסרה כניסה בוקטור 91";
                break;
            case "122":
                message = "חסרה כניסה בוקטור 92";
                break;
            case "123":
                message = "חסרה כניסה בוקטור 93";
                break;
            case "141":
                message = "חסרה כניסה מתאימה בקובץ פרמטרים 3.2";
                break;
            case "142":
                message = "חסרה כניסה מתאימה בקובץ פרמטרים 3.3";
                break;
            case "143":
                message = "חסרה כניסה בקובץ תחומי מועדון 3.6.1";
                break;
            case "144":
                message = "חסרה כניסה בקובץ תחומי מועדון 3.6.3";
                break;
            case "145":
                message = "חסרה כניסה בקובץ תחומי מועדון 3.6.4";
                break;
            case "146":
                message = "חסרה כניסה בקובץ תקרות לכרטיסי 4.1 PL";
                break;
            case "147":
                message = "חסרה כניסה בקובץ תקרות לכרטיסים ישראלים שאינם PLשיטה 4.2 0";
                break;
            case "148":
                message = "חסרה כניסה בקובץ תקרות לכרטיסים ישראלים שאינם PLשיטה 4.3 1";
                break;
            case "149":
                message = "חסרה כניסה בקובץ תקרות לכרטיסי תייר 4.4";
                break;
            case "150":
                message = "חסרה כניסה בקובץ כרטיסים תקפים -ישראכרט";
                break;
            case "151":
                message = "חסרה כניסה בקובץ כרטיסים תקפים -כאל";
                break;
            case "152":
                message = "חסרה כניסה בקובץ כרטיסים תקפים -מנפיק עתידי";
                break;
            case "182":
                message = "שגיאה בערכי וקטור 4";
                break;
            case "183":
                message = "שגיאה בערכי וקטור 6/12";
                break;
            case "186":
                message = "שגיאה בערכי וקטור 18";
                break;
            case "187":
                message = "שגיאה בערכי וקטור 34";
                break;
            case "188":
                message = "שגיאה בערכי וקטור 64";
                break;
            case "190":
                message = "שגיאה בערכי וקטור 90";
                break;
            case "191":
                message = "נתונים לא תקינים בוקטור הרשאות מנפיק";
                break;
            case "192":
                message = "נתונים לא ולידים בסט הפרמטרים";
                break;
            case "193":
                message = "נתונים לא ולידים בקובץ פרמטרים ברמת מסוף";
                break;
            case "300":
                message = "אין הרשאה לסוג עסקה - הרשאת סולק";
                break;
            case "301":
                message = "אין הרשאה למטבע - הרשאת סולק";
                break;
            case "303":
                message = "אין הרשאת סולק לביצוע עסקה כאשר הכרטיס לא נוכח";
                break;
            case "304":
                message = "אין הרשאה לאשראי - הרשאת סולק";
                break;
            case "308":
                message = "אין הרשאה להצמדה - הרשאת סולק";
                break;
            case "309":
                message = "אין הרשאת סולק לאשראי במועד קבוע";
                break;
            case "310":
                message = "אין הרשאה להקלדת מספר אישור מראש";
                break;
            case "311":
                message = "אין הרשאה לבצע עסקאות לקוד שרות 587";
                break;
            case "312":
                message = "אין הרשאת סולק לאשראי דחוי";
                break;
            case "313":
                message = "אין הרשאת סולק להטבות";
                break;
            case "314":
                message = "אין הרשאת סולק למבצעים";
                break;
            case "315":
                message = "אין הרשאת סולק לקוד מבצע ספציפי";
                break;
            case "316":
                message = "אין הרשאת סולק לעסקת טעינה";
                break;
            case "317":
                message = "אין הרשאת סולק לטעינה/פריקה בקוד אמצעי התשלום בשילוב קוד מטבע";
                break;
            case "318":
                message = "אין הרשאת סולק למטבע בסוג אשראי זה";
                break;
            case "319":
                message = "אין הרשאת סולק לטיפ";
                break;
            case "341":
                message = "אין הרשאה לעסקה - הרשאת מנפיק";
                break;
            case "342":
                message = "אין הרשאה למטבע - הרשאת מנפיק";
                break;
            case "343":
                message = "אין הרשאת מנפיק לביצוע עסקה כאשר הכרטיס לא נוכח";
                break;
            case "344":
                message = "אין הרשאה לאשראי - הרשאת מנפיק";
                break;
            case "348":
                message = "אין הרשאה לביצוע אישור בקשה יזומה ע\"י קמעונאי";
                break;
            case "349":
                message = "אין הרשאה מתאימה לביצוע בקשה לאישור ללא עסקה J5";
                break;
            case "350":
                message = "אין הרשאת מנפיק להטבות";
                break;
            case "351":
                message = "אין הרשאת מנפיק לאשראי דחוי";
                break;
            case "352":
                message = "אין הרשאת מנפיק לעסקת טעינה";
                break;
            case "353":
                message = "אין הרשאת מנפיק לטעינה/פריקה בקוד אמצעי התשלום";
                break;
            case "354":
                message = "אין הרשאת מנפיק למטבע בסוג אשראי זה";
                break;
            case "381":
                message = "אין הרשאה לבצע עסקת contactlessמעל סכום מרבי";
                break;
            case "382":
                message = "במסוף המוגדר כשרות עצמי ניתן לבצע רק עסקאות בשירות עצמי";
                break;
            case "384":
                message = "מסוף מוגדר כרב-ספק /מוטב - חסר מספר ספק/מוטב";
                break;
            case "385":
                message = "במסוף המוגדר כמסוף סחר אלקטרוני חובה להעביר eci";
                break;
            case "401":
                message = "מספר התשלומים גדול מערך שדה מספר תשלומים מקסימלי";
                break;
            case "402":
                message = "מספר התשלומים קטן מערך שדה מספר תשלומים מינימלי";
                break;
            case "403":
                message = "סכום העסקה קטן מערך שדה סכום מינמלי לתשלום !!!";
                break;
            case "404":
                message = "לא הוזן שדה מספר תשלומים";
                break;
            case "405":
                message = "חסר נתון סכום תשלום ראשון /קבוע";
                break;
            case "406":
                message = "סה\"כ סכום העסקה שונה מסכום תשלום ראשון +סכום תשלום קבוע *מספר תשלומים";
                break;
            case "408":
                message = "ערוץ 2 קצר מ-37 תווים";
                break;
            case "410":
                message = "דחיה מסיבת dcode";
                break;
            case "414":
                message = "בעסקה עם חיוב בתאריך קבוע הוכנס תאריך מאוחר משנה מבצוע העיסקה";
                break;
            case "415":
                message = "הוזנו נתונים לא תקינים";
                break;
            case "416":
                message = "תאריך תוקף לא במבנה תקין";
                break;
            case "417":
                message = "מספר מסוף אינו תקין";
                break;
            case "418":
                message = "חסרים פרמטרים חיוניים (להודעת שגיאה זו מתווספת רשימת הפרמטרים החסרים)";
                break;
            case "419":
                message = "שגיאה בהעברת מאפיין clientInputPan";
                break;
            case "420":
                message = "מספר כרטיס לא ולידי -במצב של הזנת ערוץ 2בעסקה ללא כרטיס נוכח";
                break;
            case "421":
                message = "שגיאה כללי -נתונים לא ולידים";
                break;
            case "424":
                message = "שדה לא נומרי";
                break;
            case "425":
                message = "רשומה כפולה";
                break;
            case "426":
                message = "הסכום הוגדל לאחר ביצוע בדיקות אשראית";
                break;
            case "428":
                message = "חסר קוד שרות בכרטיס";
                break;
            case "429":
                message = "כרטיס אינו תקף לפי קובץ כרטיסים תקפים";
                break;
            case "431":
                message = "שגיאה כללית";
                break;
            case "432":
                message = "אין הראשה להעברת כרטיס דרך קורא מגנטי";
                break;
            case "433":
                message = "חיוב להעביר ב - PinPad";
                break;
            case "434":
                message = "אסור להעביר כרטיס במכשיר ה- PinPad";
                break;
            case "435":
                message = "המכשיר לא מוגדר להעברת כרטיס מגנטי CTL";
                break;
            case "436":
                message = "המכשיר לא מוגדר להעברת כרטיס EMV CTL";
                break;
            case "439":
                message = "אין הרשאה לסוג אשראי לפי סוג עסקה";
                break;
            case "440":
                message = "כרטיס תייר אינו מורשה לסוג אשראי זה";
                break;
            case "441":
                message = "אין הרשאה לביצוע סוג עסקה - כרטיס קיים בוקטור 80";
                break;
            case "442":
                message = "אין לבצע Stand-inלאימות אישור לסולק זה";
                break;
            case "443":
                message = "לא ניתן לבצע עסקת ביטול - כרטיס לא נמצא בקובץ תנועות הקיים במסוף";
                break;
            case "445":
                message = "בכרטיס חיוב מיידי ניתן לבצע אשראי חיוב מיידי בלבד";
                break;
            case "447":
                message = "מספר כרטיס שגוי";
                break;
            case "448":
                message = "חיוב להקליד כתובת לקוח (מיקוד ,מספר בית ועיר)";
                break;
            case "449":
                message = "חיוב להקליד מיקוד";
                break;
            case "450":
                message = "קוד מבצע מחוץ לתחום, צ\"ל בתחום 1-12";
                break;
            case "451":
                message = "שגיאה במהלך בנית רשומת עסקה";
                break;
            case "452":
                message = "בעסקת טעינה/פריקה/בירור יתרה חיוב להזין שדה קוד אמצעי תשלום";
                break;
            case "453":
                message = "אין אפשרות לבטל עסקת פריקה 7.9.3";
                break;
            case "455":
                message = "לא ניתן לבצע עסקת חיוב מאולצת כאשר נדרשת בקשה לאישור (למעט תקרות)";
                break;
            case "456":
                message = "כרטיס נמצא בקובץ תנועות עם קוד תשובה 'החרם כרטיס'";
                break;
            case "457":
                message = "בכרטיס חיוב מיידי מותרת עסקת חיוב רגילה/זיכוי/ביטול";
                break;
            case "458":
                message = "קוד מועדון לא בתחום";
                break;
            case "470":
                message = "בעסקת הו\"ק סכום התשלומים גבוה משדה סכום העסקה";
                break;
            case "471":
                message = "בעסקת הו\"ק מספר תשלום תורן גדול מסה\"כ מספר התשלומים";
                break;
            case "472":
                message = "בעסקת חיוב עם מזומן חיוב להזין סכום במזומן";
                break;
            case "473":
                message = "בעסקת חיוב עם מזומן סכום המזומן צריך להיות קטן מסכום העסקה";
                break;
            case "474":
                message = "עסקת איתחול בהוראת קבע מחייבת פרמטר J5";
                break;
            case "475":
                message = "עסקת ה\"ק מחייבת אחד מהשדות: מספר תשלומים או סכום כולל";
                break;
            case "476":
                message = "עסקת תורן בהוראת קבע מחייבת שדה מספר תשלום";
                break;
            case "477":
                message = "עסקת תורן בהוראת קבע מחייבת מספר מזהה של עסקת איתחול";
                break;
            case "478":
                message = "עסקת תורן בהוראת קבע מחייבת מספר אישור של עסקת איתחול";
                break;
            case "479":
                message = "עסקת תורן בהוראת קבע מחייבת שדות תאריך וזמן עסקת איתחול";
                break;
            case "480":
                message = "חסר שדה מאשר עסקת מקור";
                break;
            case "481":
                message = "חסר שדה מספר יחידות כאשר העסקה מתבצעת בקוד אמצעי תשלום השונה ממטבע";
                break;
            case "482":
                message = "בכרטיס נטען מותרת עסקת חיוב רגילה/זיכוי/ביטול/פריקה/טעינה/בירור יתרה";
                break;
            case "483":
                message = "עסקה עם כרטיס דלק במסוף דלק חיוב להזין מספר רכב";
                break;
            case "484":
                message = "מספר רכב המוקלד שונה ממספר הרכב הצרוב ע\"ג הפס המגנטי/מספר בנק שונה מ-012/ספרות שמאליות של מספר הסניף שונה מ-44";
                break;
            case "485":
                message = "מספר רכב קצר מ- 6ספרות /שונה ממספר הרכב המופיע ע\"ג ערוץ 2 (פוזיציה 34 בערוץ 2) כרטיס מאפיין דלק של לאומי קארד";
                break;
            case "486":
                message = "ישנה חובת הקלדת קריאת מונה (פוזיציה 30בערוץ )2כרטיס מאפיין דלק של לאומי קארד";
                break;
            case "487":
                message = "רק במסוף המוגדר כדלק דו שלבי ניתן להשתמש בעדכון אובליגו";
                break;
            case "489":
                message = "בכרטיס דלקן מותרת עסקת חיוב רגילה בלבד (עסקת ביטול אסורה)";
                break;
            case "490":
                message = "בכרטיסי דלק/דלקן/דלק מועדון ניתן לבצע עסקאות רק במסופי דלק";
                break;
            case "491":
                message = "עסקה הכוללת המרה חייבת להכיל את כל השדות conversion_rate_06, conversion_rate_09, conversion_currency_51";
                break;
            case "492":
                message = "אין המרה על עסקאות שקל/דולר";
                break;
            case "493":
                message = "בעסקה הכוללת הטבה חיוב שיהיו רק אחד מהשדות הבאים: סכום הנחה/מספר יחידות/% ההנחה";
                break;
            case "494":
                message = "מספר מסוף שונה";
                break;
            case "495":
                message = "אין הרשאת fallback";
                break;
            case "496":
                message = "לא ניתן להצמיד אשראי השונה מאשראי קרדיט/תשלומים";
                break;
            case "498":
                message = "כרטיס ישראכרט מקומי הספרטור צ\"ל בפוזיציה 18";
                break;
            case "500":
                message = "העסקה הופסקה ע\"י המשתמש";
                break;
            case "504":
                message = "חוסר התאמה בין שדה מקור נתוני הכרטיס לשדה מספר כרטיס";
                break;
            case "505":
                message = "ערך לא חוקי בשדה סוג עסקה";
                break;
            case "506":
                message = "ערך לא חוקי בשדה eci";
                break;
            case "507":
                message = "סכום העסקה בפועל גבוה מהסכום המאושר";
                break;
            case "509":
                message = "שגיאה במהלך כתיבה לקובץ תנועות";
                break;
            case "512":
                message = "לא ניתן להכניס אישור שהתקבל ממענה קולי לעסקה זו";
                break;
            case "551":
                message = "מסר תשובה אינו מתאים למסר הבקשה";
                break;
            case "552":
                message = "שגיאה בשדה 55";
                break;
            case "553":
                message = "התקבלה שגיאה מהטנדם";
                break;
            case "554":
                message = "במסר התשובה חסר שדה mcc_18";
                break;
            case "555":
                message = "במסר התשובה חסר שדה response_code_25";
                break;
            case "556":
                message = "במסר התשובה חסר שדה rrn_37";
                break;
            case "557":
                message = "במסר התשובה חסר שדה comp_retailer_num_42";
                break;
            case "558":
                message = "במסר התשובה חסר שדה auth_code_43";
                break;
            case "559":
                message = "במסר התשובה חסר שדה f39_response_39";
                break;
            case "560":
                message = "במסר התשובה חסר שדה authorization_no_38";
                break;
            case "561":
                message = "במסר התשובה חסר/ריק שדה additional_data_48.solek_auth_no";
                break;




            case "562":
                message = "במסר התשובה חסר אחד מהשדות conversion_amount_06, conversion_rate_09, conversion_currency_51";
                break;
            case "563":
                message = "ערך השדה אינו מתאים למספרי האישור שהתקבלו auth_code_43";
                break;
            case "564":
                message = "במסר התשובה חסר/ריק שדה additional_amunts54.cashback_amount";
                break;
            case "565":
                message = "אי-התאמה בין שדה 25לשדה 43";
                break;
            case "566":
                message = "במסוף המוגדר כתומך בדלק דו-שלבי יש חובה להחזיר שדות 90,119";
                break;
            case "567":
                message = "שדות 25,127לא תקינים במסר עידכון אובליגו במסוף המוגדר כדלק דו-שלבי";
                break;
            case "598":
                message = "ERROR_IN_NEG_FILE";
                break;
            case "599":
                message = "שגיאה כללית";
                break;
            case "700":
                message = "עסקה נדחתה ע\"י מכשיר PinPad";
                break;
            case "701":
                message = "שגיאה במכשיר pinpad";
                break;
            case "702":
                message = "יציאת com לא תקינה";
                break;
            case "703":
                message = "PINPAD_TransactionError";
                break;
            case "704":
                message = "PINPAD_TransactionCancelled";
                break;
            case "705":
                message = "PINPAD_UserCancelled";
                break;
            case "706":
                message = "PINPAD_UserTimeout";
                break;
            case "707":
                message = "PINPAD_UserCardRemoved";
                break;
            case "708":
                message = "PINPAD_UserRetriesExceeded";
                break;
            case "709":
                message = "PINPAD_PINPadTimeout";
                break;
            case "710":
                message = "PINPAD_PINPadCommsError";
                break;
            case "711":
                message = "PINPAD_PINPadMessageError";
                break;
            case "712":
                message = "PINPAD_PINPadNotInitialized";
                break;
            case "713":
                message = "PINPAD_PINPadCardReadError";
                break;
            case "714":
                message = "PINPAD_ReaderTimeout";
                break;
            case "715":
                message = "PINPAD_ReaderCommsError";
                break;
            case "716":
                message = "PINPAD_ReaderMessageError";
                break;
            case "717":
                message = "PINPAD_HostMessageError";
                break;
            case "718":
                message = "PINPAD_HostConfigError";
                break;
            case "719":
                message = "PINPAD_HostKeyError";
                break;
            case "720":
                message = "PINPAD_HostConnectError";
                break;
            case "721":
                message = "PINPAD_HostTransmitError";
                break;
            case "722":
                message = "PINPAD_HostReceiveError";
                break;
            case "723":
                message = "PINPAD_HostTimeout";
                break;
            case "724":
                message = "PINVerificationNotSupportedByCard";
                break;
            case "725":
                message = "PINVerificationFailed";
                break;
            case "726":
                message = "שגיאה בקליטת קובץ config.xml";
                break;
            case "730":
                message = "מכשיר אישר עסקה בניגוד להחלטת אשראית";
                break;
            case "731":
                message = "כרטיס לא הוכנס";
                break;
            case "777":
                message = "תקין, ניתן להמשיך";
                break;
            default:
                message = "שגיאה כללית";
                break;
        }
        return message + ". \n" + "מספר שגיאה: " + str;
    }
}
