# Android ToDoList App

## 개요
1인 개발입니다.

제작 기간 : 2023.12 ~ 2023.12

## UI/UX
<img src="https://github.com/seungwoo505/ToDoList/blob/main/MotionImage.gif"/>

<https://github.com/seungwoo505/ToDoList/blob/main/MotionImage.mp4>

깔끔한 영상을 보고 싶으시면 위의 링크를 통해 확인해주시면 됩니다.

## 설명
할 일과 완료해야할 날짜를 입력합니다. 
그리고 저장하게되면 할 일을 입력한 시점도 저장해 그 날부터 완료해야할 날짜를 계산해 기준으로 잡고 시간이 지남에 따라 퍼센트가 기준에 따라 증가하게됩니다.

할 일과 기간 그리고 진행도(시간 흐름)가 표시되고 X를 통해 삭제도 가능합니다.

## 개발 고민
할 일이라고하면 기간이 정해져있을 가능성이 높습니다. 그래서 완료 날짜를 알려주고 남은 기간을 바를 통해 %로 표시해줘서 남은 기간을 확실하게 직관적으로 알 수 있게 했습니다.

수업 내에서 배운 것을 최대한 이용하면서 앱을 개발해보라는게 과제였는데 최대한 이용할 수 있는게 가계부나 ToDoList라고 생각했습니다.

그 중 ToDoList를 선택한 이유는 ToDoList에 프로그래스 바를 이용하여 진행도를 넣는 방식이 참신할 것이라고 판단하여 선택했습니다.

## 사용된 개념
- Fragment
- RecyclerView
- Adapter
- Database(SQLite)
- ProgressBar
- DatePickerDialog
