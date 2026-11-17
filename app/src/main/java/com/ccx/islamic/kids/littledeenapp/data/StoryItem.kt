package com.ccx.islamic.kids.littledeenapp.data



import android.os.Parcelable
import androidx.annotation.DrawableRes
import com.ccx.islamic.kids.littledeenapp.R
import kotlinx.parcelize.Parcelize

@Parcelize
data class StoryItem(
    val id: String,
    val title: String,
    val theme: String,
    val content: String,
    val moralLesson: String,
    val islamicTeaching: String,
    @DrawableRes val imageRes: Int = R.drawable.avatar_boy_1 // 👈 Default local illustration
) : Parcelable {
    companion object {
        fun getDefaultStories(): List<StoryItem> {
            return listOf(
                StoryItem(
                    id = "story_1",
                    title = "1. The Honest Merchant",
                    theme = "Honesty & Truthfulness",
                    content = "The marketplace in the city was always a symphony of sounds: the clinking of copper, the calls of merchants, and the sweet laughter of children playing in the aisles. \n\nYoung Ahmad loved sitting at his father’s fruit stall, where the scent of ripe oranges filled the air. He took great pride in stacking the shiny red apples into perfect, wobbly pyramids. One busy afternoon, a wealthy man in a silk robe rushed up, bought a large basket of dates, and handed Ahmad a thick stack of coins. In his haste, the man accidentally dropped an extra gold coin and vanished into the bustling crowd before Ahmad could shout, 'Wait!'\n\nAhmad looked at the extra gold coin in his palm. It was enough to buy that wooden sword he had been dreaming of for weeks. His heart raced. 'Nobody saw me,' he whispered to himself. But then, he felt a strange, heavy feeling in his chest—a reminder of his father’s words: 'Allah sees everything, Ahmad. A merchant who cheats is not a merchant Allah loves.'\n\nWith a deep breath, Ahmad gripped the coin and sprinted into the sea of people. He pushed past carts and dodged merchants, his eyes scanning for the silk robe. He finally found the man near the city gate. When Ahmad returned the coin, the man’s eyes widened in surprise. He didn't just thank Ahmad; he told the whole marketplace about the boy's amazing integrity.\n\nThat evening, Ahmad’s father didn't just hug him—he looked at him with a new sense of respect. Ahmad realized that being honest didn't just make him feel light and happy; it made him a hero in Allah’s eyes. He learned that truth is the greatest treasure one can ever own.",
                    moralLesson = "Being honest means doing the right thing, even when it is difficult and no one is watching. Honesty builds trust and makes our hearts peaceful.",
                    islamicTeaching = "The Prophet Muhammad ﷺ said: “Truthfulness leads to righteousness.” Honesty is a beautiful Sunnah that earns us the love of Allah and the respect of all people.",
                    imageRes = R.drawable.story_1
                ),

                StoryItem(
                    id = "story_2",
                    title = "2. The Kind Neighbor",
                    theme = "Kindness & Compassion",
                    content = "Amina lived next door to an old lady named Mrs. Noor. \n\nEvery Friday, Amina watched from her window. Mrs. Noor would walk slowly to her house. She carried two big, heavy bags of food. It looked very hard for her. She stopped many times to rest. Amina felt sad. She wanted to help, but she was a little bit shy.\n\nThen, Amina remembered that being kind makes Allah happy. \n\nOne Friday, Amina ran to the gate. She stood there and waited. When Mrs. Noor came, Amina smiled. 'Hello, Mrs. Noor! Let me help you carry those bags,' Amina said.\n\nMrs. Noor looked up. A big, happy smile grew on her face. 'Oh, thank you, Amina! That is so sweet of you.'\n\nAmina took the bags. They were heavy, but her heart felt light and happy. When they reached the door, Mrs. Noor gave Amina a cookie. From that day on, Amina helped her every week. She learned that a small act of kindness can make a neighbor feel very happy and loved.\n\nHelping others is like planting a seed of love that grows into a beautiful tree of friendship.",
                    moralLesson = "Helping others, even in small ways, spreads happiness. Children can show kindness by sharing smiles, helping classmates, or assisting family members.",
                    islamicTeaching = "The Prophet ﷺ said: “He is not a believer whose neighbor is not safe from his harm.” Kindness in Islam means caring for our neighbors and being a good friend to them.",
                    imageRes = R.drawable.story_2
                ),

                StoryItem(
                    id = "story_3",
                    title = "3. The Patient Little Gardener",
                    theme = "Patience (Sabr)",
                    content = "Bilal loved his backyard. It was big, sunny, and full of space. One day, his father gave him a small bag of sunflower seeds. 'If you plant these and take care of them, they will grow into giant yellow flowers,' his father said.\n\nBilal was so excited! He dug a small hole in the dirt, dropped the seed inside, and covered it up. He gave it a big splash of water. Then, Bilal sat down and waited. He waited for ten minutes. Nothing happened. He waited for one hour. Still, nothing happened.\n\n'Why is it not growing?' Bilal asked, feeling a little sad. He wanted to dig up the dirt to check if the seed was still there. \n\nHis grandmother sat next to him and put her arm around his shoulders. 'Bilal,' she said softly, 'seeds are like small gifts from Allah. They need time to sleep in the dirt and time to drink the water. If you dig them up, they cannot grow. You must have patience.'\n\nBilal decided to try again. He watered the spot every single day. He made sure the birds didn't peck at the dirt. Even when he felt like giving up, he remembered his grandmother's words. He learned to wait, day after day, week after week.\n\nFinally, one sunny morning, Bilal ran outside and saw a tiny, bright green leaf peeking out from the ground. He clapped his hands and cheered! It was the smallest leaf, but it was the most beautiful thing he had ever seen. Bilal learned that waiting is not a waste of time. It is a way to show that we trust Allah, and that good things come to those who wait and work hard.",
                    moralLesson = "Good things take time. Being patient means waiting calmly and not giving up, even when we feel like it. Allah loves those who are patient.",
                    islamicTeaching = "Allah tells us in the Qur’an that He is with those who have patience. Patience helps us stay calm and strong when we are waiting for something good to happen.",
                    imageRes = R.drawable.story_3
                ),

                StoryItem(
                    id = "story_4",
                    title = "4. The Thankful Sparrow",
                    theme = "Gratitude (Shukr)",
                    content = "High up in an old oak tree lived a tiny brown sparrow named Pip. Pip was very small, and his wings were not very strong. During the cold winter, the wind would blow hard, and it was very difficult for Pip to find food. Some days, he only found a few tiny crumbs, and other days, he found nothing at all.\n\nBut even when his tummy was a little empty, Pip would hop onto a branch as the sun went down. He would puff out his chest, look up at the sky, and chirp the most beautiful, happy songs you have ever heard. \n\nA young boy named Omar lived nearby. He often watched Pip from his bedroom window. Omar noticed that the sparrow was always chirping happily, even when the weather was freezing and food was hard to find. One day, Omar asked his teacher, 'Teacher, why is the little bird so happy? He doesn't have a warm house or a big kitchen full of food like we do.'\n\nThe teacher smiled and pointed at the bird. 'Pip knows something very special, Omar. He knows that Allah is the One who takes care of every creature. He chirps because he trusts that Allah will provide for him, and he is thankful for every single crumb he finds.'\n\nOmar felt a little bit ashamed because he sometimes complained when his lunch wasn't his favorite. The next morning, when Omar sat down for breakfast, he stopped before he took his first bite. He looked at his warm bowl of porridge and whispered, 'Alhamdulillah.' \n\nHe realized that saying thank you made his food taste better and his heart feel bigger. From that day on, Omar and Pip were both happy. Whenever Omar saw the sparrow, he would put out a few extra seeds, and Pip would chirp a 'Thank You' song just for him. Both of them learned that when you say Alhamdulillah, Allah fills your day with more blessings than you can count.",
                    moralLesson = "Gratitude means saying thank you to Allah for everything, even the small things. When we are thankful, we feel happier, and Allah gives us even more blessings.",
                    islamicTeaching = "Allah promises in the Qur’an: 'If you are grateful, I will give you more.' Saying Alhamdulillah is the key to a happy and blessed heart.",
                    imageRes = R.drawable.story_4
                ),

                StoryItem(
                    id = "story_5",
                    title = "5. The Sharing Lunchbox",
                    theme = "Sharing & Generosity",
                    content = "It was lunchtime at school, the best part of the day! Zayd sat at his favorite table, ready to open his lunchbox. Inside, his mother had packed his favorite meal: a delicious, cheesy sandwich and a bright red apple. Zayd was very hungry and was already imagining his first bite.\n\nSuddenly, he looked across the table. His friend, Ali, was sitting there all by himself. Ali was staring at his empty desk. He had forgotten his lunchbox at home, and his tummy gave a tiny, sad rumble. Ali looked down, trying to hide his face. \n\nZayd looked at his sandwich. He was so hungry, but then he remembered what his teacher said: 'Sharing is a gift that makes two hearts happy instead of one.' Zayd took a deep breath, smiled, and slid his lunchbox toward Ali. 'Hey Ali! Let’s share my lunch today. It’s too big for me anyway!'\n\nAli’s eyes lit up. 'Are you sure, Zayd?' he asked. 'Yes, of course!' Zayd said. They split the sandwich and shared the apple. They sat there talking, laughing, and eating together. Zayd realized that the sandwich tasted even better because he was sharing it. \n\nLater that day, another friend came over and shared his cookies with Zayd. Zayd smiled. He realized that when you give a little, Allah brings kindness back to you in many different ways. Sharing is not losing; it is the best way to make a friend.",
                    moralLesson = "Sharing creates friendship and love. When we give what we have to someone in need, Allah puts more blessings in our life.",
                    islamicTeaching = "The Prophet Muhammad ﷺ taught us that the best of people are those who are most helpful and generous to others.",
                    imageRes = R.drawable.story_5
                ),

                StoryItem(
                    id = "story_6",
                    title = "6. The Forgiving Brother",
                    theme = "Forgiveness & Anger Control",
                    content = "Hassan was busy building a giant tower with his favorite toy blocks. It was the tallest tower he had ever made, and it had a little car parked right on the top. He had worked on it for almost an hour! \n\nSuddenly, his little brother, Sami, came running through the room, waving his arms in the air. Oops! Sami’s hand hit the tower. Crash! All the blocks tumbled down with a loud noise, and the little car went flying across the room. \n\nHassan felt his face get hot. He was very, very angry. He stood up, clenched his fists, and opened his mouth to shout. He wanted to tell Sami that he was clumsy and mean. But then, he remembered a story his mother told him about how the Prophet ﷺ always chose kindness over anger. Hassan took a deep breath, one, two, three times. \n\nHe looked at Sami. Sami looked scared, and his bottom lip was shaking. He whispered, 'I am so sorry, Hassan. Please don't be mad.' Hassan looked at the mess on the floor and then back at his brother’s sad face. He realized that a pile of blocks wasn't as important as his brother’s heart. \n\n'It is okay, Sami,' Hassan said softly. 'Let's build it again, together!' Sami’s face brightened up instantly. He ran to help Hassan pick up the pieces. That night, Hassan felt very peaceful. He learned that being strong doesn't mean winning an argument—it means being big enough to forgive. Forgiveness is like a magic key that turns anger into peace.",
                    moralLesson = "Forgiveness makes hearts peaceful. Being able to forgive others, especially our family, shows that we have a kind and strong heart.",
                    islamicTeaching = "Allah loves those who forgive others. When we let go of our anger and forgive, Allah rewards us with peace and closeness to Him.",
                    imageRes = R.drawable.story_6
                ),

                StoryItem(
                    id = "story_7",
                    title = "7. The Trusting Traveler",
                    theme = "Trust in Allah (Tawakkul)",
                    content = "Maryam packed her little yellow backpack the night before her big school trip. Tomorrow, her class was going to visit a tall nature mountain and see wild deer! She had packed her water bottle, a warm green jacket, and her favorite notebook.\n\nYet when Maryam lay down in bed, her tummy felt like it was doing somersaults. What if she got lost in the woods? What if the yellow bus broke down in the rain? Maryam curled up under her blanket, feeling small and worried.\n\nHer mother noticed the night light was still on. She sat down beside Maryam and gently stroked her hair. 'What is troubling you, my sweet bird?'\n\n'Mom, I am scared something bad will happen on the trip,' Maryam whispered.\n\nHer mother smiled warmly. 'Maryam, do you remember what Tawakkul means? It means you prepare your best, pack your bag, tie your shoes, and then hand your worries over to Allah. Say, Bismillah, I place my trust in Allah. He watches over every traveler on every road.'\n\nMaryam closed her eyes and repeated the words softly. She imagined a soft, warm shield around her. That night, she slept like a little stone, peaceful and safe.\n\nThe next day on the mountain was wonderful! The sun was golden, the deer ate grass right near their trail, and when the bus bumped over rocky paths, Maryam didn't feel afraid at all. She knew she was never alone, because Allah was protecting every step.",
                    moralLesson = "Trusting Allah means preparing your best, doing what you can, and letting go of worry because Allah is the best protector.",
                    islamicTeaching = "Allah says in the Qur’an that whoever puts their trust in Allah, He will be enough for them. Trusting Allah brings calmness to our hearts.",
                    imageRes = R.drawable.story_7
                ),

                StoryItem(
                    id = "story_8",
                    title = "8. The Respectful Daughter",
                    theme = "Respect for Parents",
                    content = "Layla loved drawing cartoon animals after school. One chilly Tuesday evening, she was just finishing a colorful sketch of a baby lion when she heard the front door click open.\n\nHer mother walked inside. Her shoulders looked heavy, her eyes looked tired, and she set her work bag down with a quiet sigh. She had been on her feet at the hospital all day caring for sick people.\n\nLayla looked at her unfinished drawing. She really wanted to color the lion’s fluffy tail before dinner. But then she looked at her mother’s tired feet. Layla put down her orange crayon immediately.\n\nShe quietly walked to the kitchen. She filled a clean glass with fresh, cool water. Then she found her mother’s soft pink slippers from the bedroom and carried them both into the living room.\n\n'Assalamu Alaikum, Mom,' Layla whispered with a bright smile. 'Here is some water for you, and your cozy slippers!'\n\nHer mother looked at the glass, then looked at Layla. All the tiredness vanished from her eyes, replaced by tears of pure joy. She pulled Layla into a big, warm hug that smelled like lavender. 'May Allah bless you, my dear child, and grant you the highest place in Paradise for your sweet manners.'\n\nLayla felt an amazing warm glow inside her chest. Her drawing could wait, but seeing her mother smile like that made Layla feel like the richest girl in the world.",
                    moralLesson = "Respecting parents means serving them happily, noticing when they are tired, and bringing them joy without being asked.",
                    islamicTeaching = "The Prophet Muhammad ﷺ taught that Paradise lies at the feet of mothers. Serving and honoring our parents is one of the highest deeds in Islam.",
                    imageRes = R.drawable.story_8
                ),

                StoryItem(
                    id = "story_9",
                    title = "9. The Helpful Classmate",
                    theme = "Helping Others",
                    content = "Omar loved story time in his third-grade classroom. He was quick at reading and loved adventure books. One Monday, a new boy named Zain joined their class from another country. Zain had curly hair, wore a green sweater, and spoke very quietly.\n\nWhen the teacher asked Zain to read a sentence from the board, Zain stood up. His hands trembled slightly, holding the book tight. He tried to read the words, but he stumbled on the letters. Two boys sitting in the back corner began to chuckle and whisper.\n\nZain looked down at his shoes, his cheeks turning bright red. He looked like he wanted to disappear under his desk. Omar saw this and felt a sharp sting in his chest. He remembered his grandfather’s wise advice: 'When you see someone falling, you don’t laugh—you become their helping hand.'\n\nAt recess, while everyone else ran to the soccer field, Omar took his seat right next to Zain. He smiled warmly. 'Hey Zain! English was hard for me when I was little too. Want to read this comic book together?'\n\nZain looked up, surprised and grateful. Day after day, Omar sat with him, pointing at difficult words, sounding out syllables slowly, and laughing together when funny characters spoke.\n\nThree weeks later, the teacher called on Zain again. This time, Zain stood tall and read the whole paragraph loudly and clearly! The entire class clapped. Zain looked straight at Omar and grinned. Omar realized that giving a little bit of your time to help a friend grow is the best kind of charity you can give.",
                    moralLesson = "Helping others when they struggle builds deep friendship, confidence, and self-respect. True strength is using what you know to lift someone else up.",
                    islamicTeaching = "The Prophet Muhammad ﷺ said: 'Allah continues to help a person as long as that person helps his brother.' Kindness and assistance to others is pure worship.",
                    imageRes = R.drawable.story_9
                ),

                StoryItem(
                    id = "story_10",
                    title = "10. The Boy with Beautiful Manners",
                    theme = "Good Character (Akhlaq)",
                    content = "Seven-year-old Yusuf lived in a quiet neighborhood full of green gardens and chirping birds. Wherever Yusuf walked, he carried a bright, sunny smile on his face. When he passed the mail carrier, he called out, 'Assalamu Alaikum, Uncle!' When the baker handed his family fresh bread, Yusuf looked into his eyes and said, 'JazakAllah Khair!' with a joyful nod.\n\nOne bright Saturday morning, Yusuf went to the grocery market with his father. The supermarket aisle was crowded. An elderly man in front of them accidentally dropped an entire carton of fresh oranges. Oranges tumbled across the floor in every direction!\n\nWhile other shoppers stepped around the mess, Yusuf immediately knelt down on his knees. He began rolling the oranges back, brushing the dust off them, and placing them gently into the old man’s basket. When he handed the last orange over, Yusuf smiled and said, 'Here you go, sir! No problem at all.'\n\nThe elderly man was amazed. He touched his hand to his heart and said to Yusuf's father, 'Sir, your son has the most gentle and beautiful manners I have seen in years. Where did he learn such kindness?'\n\nYusuf’s father smiled proudly and replied, 'He learns from the Sunnah of our beloved Prophet Muhammad ﷺ.'\n\nOn the walk home, holding his father's warm hand, Yusuf understood something deeply. Good manners are not just words you say to be polite. Good manners are like perfume—wherever you go, you leave a sweet and peaceful scent behind that makes everyone love Islam through your actions.",
                    moralLesson = "Good character and polite manners make life peaceful, bright, and joyful. Manners show our faith through our everyday actions and words.",
                    islamicTeaching = "The Prophet Muhammad ﷺ said: 'I was sent only to perfect good character.' Beautiful manners are the heaviest good deeds on the Scales on the Day of Judgment.",
                    imageRes = R.drawable.story_10
                )
            )
        }
    }
}