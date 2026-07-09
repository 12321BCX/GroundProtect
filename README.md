# GroundProtect - Minecraft Paper 插件

永久方塊保護系統 - 保護你的伺服器草方塊 (或任何方塊)

## 功能

✅ **方塊鎖定** - 單個或區域保護
✅ **防爆保護** - 抵禦所有傷害來源
✅ **選取工具** - 直觀的區塊選擇
✅ **區域保護** - 兩點式區域定義
✅ **權限系統** - 細粒度權限控制
✅ **數據持久化** - YAML 格式存儲
✅ **特定類型保護** - 只保護特定方塊類型

## 安裝

1. 構建插件：
```bash
mvn clean package
```

2. 將生成的 JAR 文件複製到 `plugins/` 目錄
```bash
cp target/GroundProtect-1.0.0.jar /你的伺服器路徑/plugins/
```

3. 重啟伺服器

## 快速開始

### 獲得選取工具
```
/gp wand
```

### 保護單個方塊
```
1. 左鍵點擊要保護的方塊
2. /gp lock
```

### 保護區域
```
1. /gp pos1        # 設置區域第一個角落
2. /gp pos2        # 設置區域對角
3. /gp lock        # 鎖定整個區域
```

### 只保護特定方塊
```
/gp lock grass_block    # 只鎖定草方塊
/gp lock oak_log        # 只鎖定橡木原木
```

## 指令列表

| 指令 | 權限 | 說明 |
|------|------|------|
| `/gp wand` | `groundprotect.admin.wand` | 獲得選取工具（木棒） |
| `/gp lock [blockType]` | `groundprotect.admin.lock` | 鎖定已選取的方塊或區域 |
| `/gp unlock` | `groundprotect.admin.unlock` | 解除已選取方塊的鎖定 |
| `/gp remove` | `groundprotect.admin.remove` | 完全刪除保護數據 |
| `/gp pos1` | `groundprotect.admin.lock` | 設置區域選擇第一點 |
| `/gp pos2` | `groundprotect.admin.lock` | 設置區域選擇第二點 |
| `/gp info` | `groundprotect.use` | 查看方塊保護信息 |
| `/gp reload` | `groundprotect.admin.reload` | 重新讀取配置文件 |

## 權限系統

```
groundprotect.use
  - 允許玩家查看保護信息

groundprotect.admin.*
  - 所有管理員指令 (包含以下所有權限)

groundprotect.admin.lock
  - 鎖定方塊/區域

groundprotect.admin.unlock
  - 解鎖方塊

groundprotect.admin.remove
  - 刪除保護

groundprotect.admin.wand
  - 使用選取工具

groundprotect.admin.reload
  - 重新讀取配置
```

## 配置文件

配置文件位置：`plugins/GroundProtect/config.yml`

```yaml
plugin:
  debug: false
  language: zh_TW

wand:
  material: STICK
  name: "§aGroundProtect 選取工具"
  lore:
    - "§7左鍵選取方塊"
    - "§7右鍵查看資訊"

messages:
  prefix: "§a[GroundProtect]§r "
  locked: "§a方塊已鎖定"
  unlocked: "§c方塊已解除鎖定"
  block_protected: "§c此方塊受到保護"
```

數據保存：`plugins/GroundProtect/data.yml`

## 使用示例

### 例子 1：保護單個草方塊

```
1. /gp wand                  ← 獲得選取工具
2. 左鍵點擊想保護的草方塊     ← 選取
3. /gp lock                  ← 鎖定
✅ 完成！此草方塊永遠不會被破壞
```

### 例子 2：保護區域內的所有方塊

```
1. /gp pos1                  ← 站在第一個角落
2. /gp pos2                  ← 走到對角
3. /gp lock                  ← 鎖定整個區域
✅ 區域內所有方塊都受保護
```

### 例子 3：只保護特定類型的方塊

```
1. /gp pos1                  ← 設置第一點
2. /gp pos2                  ← 設置第二點
3. /gp lock oak_log          ← 只鎖定橡木原木
✅ 區域內只有橡木原木受保護
```

### 例子 4：查看方塊信息

```
1. 使用選取工具右鍵點擊方塊
✅ 顯示座標、方塊類型和保護狀態
```

## 技術詳情

- **最低版本**: Paper 1.20.1
- **Java 版本**: 11+
- **構建工具**: Maven
- **數據格式**: YAML

## 保護機制

本插件保護方塊免受以下傷害：

✅ 玩家挖掘
✅ 爆炸（TNT、礫石等）
✅ 火災蔓延
✅ 流體流動（水、熔岩）
✅ 活塞推動/拉動
✅ 方塊生長
✅ 實體修改（生物踐踏等）

## 常見問題

**Q: 如何解鎖已鎖定的方塊?**
A: 使用選取工具點擊方塊，然後執行 `/gp unlock`

**Q: 如何刪除所有保護?**
A: 刪除 `plugins/GroundProtect/data.yml` 文件並執行 `/gp reload`

**Q: 支持哪些版本?**
A: 目前支持 Paper 1.20.1，理論上支持 1.20+ 版本

**Q: 如何只保護特定方塊?**
A: 使用 `/gp lock block_name`，例如 `/gp lock grass_block`

**Q: 數據會保存到哪裡?**
A: 保存在 `plugins/GroundProtect/data.yml`，重啟伺服器時自動加載

## 故障排除

### 保護不起作用
1. 檢查玩家是否擁有正確的權限
2. 確認方塊已被正確鎖定（使用 `/gp info`）
3. 檢查服務器日誌是否有錯誤信息

### 性能問題
1. 不要在同一位置鎖定太多方塊
2. 使用區域鎖定而不是單個方塊鎖定

## 授權

MIT License

## 開發者

Developer

## 支持

如有問題或建議，請提交 Issue 或 Pull Request。