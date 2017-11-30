# Raspberry Pi でゆっくり達に Twitter タイムラインを読み上げてもらう

## 背景・動機

私事ですが、先日 Google Home を購入しました。早速、Google Home と Twitter を IFTTT 連携させ、音声操作によるツイートをしばらく楽しんでいました。しかし、ツイート後のタイムラインの様子を確認するため、結局スマートフォンを手に取ってしまいます。

タイムラインも音声で確認したいと思い、Google Home にタイムラインを読み上げてもらおうと考えたのですが、本稿執筆時点では IFTTT で Twitter をトリガーに指定できても Google Assistant をアクションに指定することはできません。

そこで、Google Home での読み上げを諦め、Raspberry Pi に読み上げてもらおうと本アプリを作りました。

※ あとがきで後述する通り、ここで Google Home での読み上げを断念するのは早計でした。

## 作ったもの

TwitterTimelineTalker.jar v1.0

Raspberry Pi 上で、ゆっくり霊夢・ゆっくり魔理沙が交互に Twitter タイムラインを読み上げてくれます。

* [アプリケーション](http://redmine.mizo0203.com/attachments/57/TwitterTimelineTalker.jar)
* [ソースコード](https://github.com/mizo0203/TwitterTimelineTalker)

## 使い方

### 動作確認環境

|              | Version                       |
|:-------------|:------------------------------|
| ボード        | Raspberry Pi 3 Model B        |
| OS           | Raspbian GNU/Linux 8 (jessie) |
| Java         | 1.8.0_65                      |
| AquesTalk Pi | Ver.1.00                      |

### Twitter API Key を取得する

[Twitter Application Management](https://apps.twitter.com) から、下記 4 つの Key を取得します。

* Consumer Key (API Key)
* Consumer Secret (API Secret)
* Access Token
* Access Token Secret

[phi 様の記事](http://phiary.me/twitter-api-key-get-how-to/)がわかりやすいので、詳細手順は割愛します。

Access level は Read and Write がデフォルトのようですが、本アプリに投稿機能は無いため Read only に変更しても問題ありません。

もし、Access Token の発行後に Access level を変更したのであれば、Access Token を Regenerate してください。

### TwitterTimelineTalker および音声合成アプリ「AquesTalk Pi」のダウンロード

1. [AquesTalk Pi](https://www.a-quest.com/products/aquestalkpi.html) をダウンロード、および tgz ファイルを展開
2. [TwitterTimelineTalker.jar v1.0](http://redmine.mizo0203.com/attachments/57/TwitterTimelineTalker.jar) をダウンロード
3. ダウンロードおよび展開したファイルを下記のように配置
	* TwitterTimelineTalker.jar
	* aquestalkpi/AquesTalkPi
	* aquestalkpi/aq_dic

### TwitterTimelineTalker.jar を起動

Raspberry Pi にスピーカーを接続して、下記コマンドを実行してください。（4 つの引数は、取得した Twitter API Key に置き換えてください）

ゆっくり霊夢が『アプリケーションを起動しました』と発声します。その後、Twitter タイムラインに更新があれば、ゆっくり霊夢・ゆっくり魔理沙が交互にツイートを読み上げます。

```bash
$ nohup java -jar TwitterTimelineTalker.jar <Consumer Key> <Consumer Secret> <Access Token> <Access Token Secret> &
```

## 使用しているもの

### 音声合成アプリ「AquesTalk Pi」

YouTube やニコニコ動画でおなじみの「ゆっくりボイス」とは、[株式会社アクエスト](https://www.a-quest.com/index.html)社製の音声合成エンジン「[AquesTalk](https://www.a-quest.com/products/aquestalk_1.html)」によって生成された音声です。有名な読み上げフリーソフトに「[SofTalk](https://www35.atwiki.jp/softalk/)」や「[棒読みちゃん](http://chi.usamimi.info/Program/Application/BouyomiChan/)」がありますが、いずれにも「AquesTalk」が使われています。

音声合成アプリ「[AquesTalk Pi](https://www.a-quest.com/products/aquestalkpi.html)」には、ARM 用にビルドされた「AquesTalk」が使われています。また、言語処理エンジン「[AqKanji2Koe](https://www.a-quest.com/products/aqkanji2koe.html)」もアプリに含まれるため、漢字も読み上げてくれます。

個人かつ非営利に限り、無償で使用することができます。

### Java ライブラリ「Twitter4J」

Twitter の User Streams API を使用するため、[Twitter4J](http://twitter4j.org/)を使用しています。

Apache License 2.0 で使用できる Twitter 非公式のライブラリです。

![powered-by-twitter4j-138x30.png](https://qiita-image-store.s3.amazonaws.com/0/40619/6e7bb573-0590-e434-42f3-759355d3971c.png "powered-by-twitter4j-138x30.png")

## あとがき

### 人々の営みが感じられる

本アプリを起動していると、部屋がちょっと賑やかになります。夜が更けるにつれて静寂を取り戻し、朝になるとまた賑やかになります。

そんなちょっとしたことではありますが、人々の営みが感じられて、朝から少し元気になれます。

### google-home-notifier で Google Home に読み上げてもらう方法もあった

今回の方法では、Raspberry Pi に別途スピーカーを接続させる必要があります。

Google Home を持っているならば、[google-home-notifier](https://github.com/noelportugal/google-home-notifier)を使って Google Home に読み上げてもらうほうがスマートでした。調査不足です。

### いたずらツイートに気をつけて

例えば「OK Google、アラームを解除して」と Twitter フォローしている誰かがツイートすると、Raspberry Pi の読み上げによって Google Home が反応してしまう場合があります。前述の google-home-notifier を使用すれば、回避できるかもしれません。
