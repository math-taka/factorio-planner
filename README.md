# factorio-planner

Factorioの生産計画を支援するJava製ツール


## 目標
Factorioの工場全体をシミュレーションし、生産量・必要素材・工場数を計算するツール。

- レシピ計算
- 原料集計
- 工場台数計算
- ベルト計算
- JavaFX GUI
- Android対応


## 開発状況
- [x] レシピ読み込み
- [x] 原料集計
- [x] 工場台数計算
- [ ] GUI


## 現在の状況

- Ver 0.1 完了 — レシピ計算エンジン
- Ver 0.2 完了 — RecipeCalculatorの改善
- Ver 0.3 80% — 複数アイテムの原料総計算
  - 残り: #24 循環参照によるループの検出
- Ver 0.4 完了 — JSONによるレシピ読み込み
- Ver 0.5 完了 — デモアプリケーションの作成
- Ver 0.6 完了 — Productivityへの対応
- Ver 0.7 完了 — 生産ツリーの構築
- Ver 0.8 完了 — 必要な生産設備数の計算
- **Ver 0.9 開発中 — JavaFX GUIの実装**


### Ver 0.9

現在はJavaFX GUIの実装を進めています。

- [x] UI設計
- [x] JavaFXセットアップ
- [ ] 入力UI
- [ ] Calculatorとの接続
- [ ] 計算結果の表示
- [ ] UIテスト・リファクタリング